package com.panxora.gravity.tick.exchange.publisher

import com.panxora.gravity.tick.exchange.model.ExchangeSymbol
import com.panxora.gravity.tick.exchange.model.PriceEvent
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component
import javax.jms.Topic

@Component
class PriceEventPublisher(
    @Qualifier("EXCHANGE_PRICES") private val topic: Topic,
    private val jmsTemplate: JmsTemplate,
    @Value("\${com.panxora.gravity.tick.number.price.streams:1}")
    private val numberOfPriceStreams : Int
        ) {
    fun publish(exchangeTick: PriceEvent){
        jmsTemplate.convertAndSend(topic, exchangeTick) { message ->
            val stream = exchangeTick.exchangeSymbol.calculateStream()
            message.setIntProperty("stream", stream)
            message.setStringProperty("exchange", exchangeTick.exchangeSymbol.exchange)
            message
        }
    }
    private fun ExchangeSymbol.calculateStream() : Int {
        val exchangeSum = this.exchange.toCharArray().sumBy { it.toInt() }
        val symbolSum = this.symbol.toCharArray().sumBy { it.toInt() }
        return ((exchangeSum + symbolSum) % numberOfPriceStreams) + 1
    }
}
