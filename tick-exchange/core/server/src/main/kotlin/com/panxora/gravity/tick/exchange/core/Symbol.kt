package com.panxora.gravity.tick.exchange.core

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(schema = "TICK", name = "V_PRICING_PAIRS")
class Symbol(
    @Id
    val instrumentId: Int,

    val tradePair: String,

    val exchangeName: String,

    val feedGroupId: Int
)
