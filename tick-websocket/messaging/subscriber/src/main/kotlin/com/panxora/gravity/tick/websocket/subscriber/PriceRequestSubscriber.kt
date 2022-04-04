package com.panxora.gravity.tick.websocket.subscriber

import com.panxora.gravity.tick.websocket.model.PriceRequest
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@Component
class PriceRequestSubscriber(
        private val priceRequestHandler: PriceRequestHandler
        ) {
    @JmsListener(
        destination = "PRICE_REQUEST",
        concurrency = "1",
        subscription = "price-request-group"
    )
    fun tickReceiver(priceRequest: PriceRequest){
        priceRequestHandler.handle(priceRequest)
    }
}
