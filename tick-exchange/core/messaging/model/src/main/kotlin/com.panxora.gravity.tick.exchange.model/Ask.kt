package com.panxora.gravity.tick.exchange.model

import java.io.Serializable
import java.math.BigDecimal
import java.time.Instant

data class Ask(
    override val price: BigDecimal,
    override val timestamp: Instant
) : Price, Serializable