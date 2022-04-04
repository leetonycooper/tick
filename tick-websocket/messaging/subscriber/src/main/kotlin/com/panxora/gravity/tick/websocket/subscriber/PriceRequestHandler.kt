package com.panxora.gravity.tick.websocket.subscriber

import com.panxora.gravity.tick.websocket.model.PriceRequest

interface PriceRequestHandler {
    fun handle(priceRequest: PriceRequest)
}