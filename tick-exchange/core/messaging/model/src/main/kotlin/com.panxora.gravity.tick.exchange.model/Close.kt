package com.panxora.gravity.tick.exchange.model

import java.io.Serializable
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

data class Close(
    override val price: BigDecimal,
    override val timestamp: Instant,
    val tradeId: String,
    val exchangeTimestamp: Instant,
    val closeDate: LocalDate
) : Price, Serializable