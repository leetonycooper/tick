package com.panxora.gravity.tick.exchange.model

import java.io.Serializable
import java.util.*

data class ExchangeCloseEvent(
    override val eventId: String = UUID.randomUUID().toString(),
    override val exchangeSymbol: ExchangeSymbol,
    val close: Close
) : PriceEvent, Serializable