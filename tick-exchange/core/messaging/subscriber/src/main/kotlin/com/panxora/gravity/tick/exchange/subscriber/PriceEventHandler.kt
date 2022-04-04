package com.panxora.gravity.tick.exchange.subscriber

import com.panxora.gravity.tick.exchange.model.PriceEvent

interface PriceEventHandler {
    fun handle(priceEvent: PriceEvent)
}