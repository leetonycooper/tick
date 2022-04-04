package com.panxora.gravity.tick.websocket

import com.panxora.gravity.tick.websocket.model.PriceRequest
import com.panxora.gravity.tick.websocket.publisher.PriceRequestPublisher
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class WebSocketController(
    private val priceRequestPublisher: PriceRequestPublisher
) {
    @MessageMapping("/ticker/{exchange}/{symbol}")
    @SendTo("/topic/{exchange}/{symbol}")
    @Throws(Exception::class)
    fun subscribe(@DestinationVariable exchange: String, @DestinationVariable symbol: String): com.panxora.gravity.tick.websocket.ExchangeTick? {
        makePriceRequest(exchange, symbol)
        return null
    }

    private fun makePriceRequest(exchange: String, symbol: String) : com.panxora.gravity.tick.websocket.ExchangeTick?{
        priceRequestPublisher.publish(
                priceRequest = PriceRequest(
                    exchange = exchange, symbol = symbol
                ))
        return null
    }
}