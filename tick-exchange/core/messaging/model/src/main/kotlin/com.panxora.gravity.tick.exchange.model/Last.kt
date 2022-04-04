package com.panxora.gravity.tick.exchange.model

import java.io.Serializable
import java.math.BigDecimal
import java.time.Instant

data class Last(
    override val price: BigDecimal,
    override val timestamp: Instant,
    val tradeId: String? = null,
    val exchangeTimestamp: String
) : Price, Serializable