package com.panxora.gravity.tick.websocket

import java.math.BigDecimal
import java.time.LocalDate

data class ExchangeClose(
    val eventId: String,
    val exchange: String,
    val symbol: String,
    val close: BigDecimal,
    val closeDate: LocalDate
) : Price
