package com.panxora.gravity.tick.exchange.model

import java.time.Instant
import java.util.*
import java.io.Serializable

data class ExchangeTickEvent(
        override val eventId: String = UUID.randomUUID().toString(),
        override val exchangeSymbol: ExchangeSymbol,
        val bid: Bid,
        val ask: Ask,
        val last: Last,
        val changeAttributedTo: TickChangeAttribution,
        val source: TickSource,
        val timestamp: Instant = Instant.now()
) : PriceEvent, Serializable