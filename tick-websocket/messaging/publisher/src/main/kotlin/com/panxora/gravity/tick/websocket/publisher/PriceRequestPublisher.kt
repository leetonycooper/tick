package com.panxora.gravity.tick.websocket.publisher

import com.panxora.gravity.tick.websocket.model.PriceRequest
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component
import javax.jms.Topic

@Component
class PriceRequestPublisher(
        @Qualifier("PRICE_REQUEST") private val topic: Topic,
        private val jmsTemplate: JmsTemplate
        ) {

    fun publish(priceRequest: PriceRequest){
        jmsTemplate.convertAndSend(topic, priceRequest)
    }
}