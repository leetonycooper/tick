package com.panxora.gravity.tick.websocket

import com.panxora.gravity.tick.exchange.model.TickChangeAttribution
import java.math.BigDecimal

data class ExchangeTick(
    val eventId: String,
    val exchange: String,
    val symbol: String,
    val bid: BigDecimal,
    val ask: BigDecimal,
    val last: BigDecimal,
    val changeAttributedTo: TickChangeAttribution
) : Price
