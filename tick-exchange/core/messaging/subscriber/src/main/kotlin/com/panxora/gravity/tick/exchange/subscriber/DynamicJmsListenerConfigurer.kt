package com.panxora.gravity.tick.exchange.subscriber

import com.panxora.gravity.tick.exchange.model.ExchangeTickEvent
import org.apache.activemq.command.ActiveMQObjectMessage
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.jms.annotation.JmsListenerConfigurer
import org.springframework.jms.config.JmsListenerEndpointRegistrar
import org.springframework.jms.config.SimpleJmsListenerEndpoint
import java.util.*
import javax.jms.MessageListener

@Configuration
class DynamicJmsListenerConfigurer(
        @Value("\${com.panxora.gravity.tick.subscription.prefixName:none}")
        val subscriptionPrefix : String,
        @Value("#{'\${com.panxora.gravity.tick.streams:1}'.split(',')}")
        val streams : Array<String>,
        val priceEventHandler: PriceEventHandler
    ) : JmsListenerConfigurer {

    override fun configureJmsListeners(registrar: JmsListenerEndpointRegistrar) {
        streams.forEach {
            val name = "$subscriptionPrefix-$it"
            val endpoint = SimpleJmsListenerEndpoint()
            endpoint.id = name
            endpoint.subscription = if(name == "none") UUID.randomUUID().toString() else name
            endpoint.concurrency = "1"
            endpoint.selector = "stream = $it"
            endpoint.destination = "EXCHANGE_PRICES"
            endpoint.messageListener = MessageListener { message ->
                if(message is ActiveMQObjectMessage){
                    val obj = message.`object`
                    if(obj is ExchangeTickEvent) {
                        LOG.trace("JMS tick received - $obj - [${message.getStringProperty("stream")}]")
                        priceEventHandler.handle(obj)
                    }
                    else{
                        LOG.error("Unexpected object type ${obj.javaClass.canonicalName} expected ${ExchangeTickEvent::class.java.canonicalName}")
                    }
                }
                else{
                    LOG.error("Unexpected message type ${message.javaClass.canonicalName} expected ${ActiveMQObjectMessage::class.java.canonicalName}")
                }
            }
            registrar.registerEndpoint(endpoint)
        }
    }

    companion object {
        private val LOG
                = getLogger(DynamicJmsListenerConfigurer::class.java)
    }
}