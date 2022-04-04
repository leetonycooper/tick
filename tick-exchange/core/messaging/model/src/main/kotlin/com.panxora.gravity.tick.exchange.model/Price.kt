package com.panxora.gravity.tick.exchange.model

import java.math.BigDecimal
import java.time.Instant

interface Price{
    val price: BigDecimal
    val timestamp: Instant
}