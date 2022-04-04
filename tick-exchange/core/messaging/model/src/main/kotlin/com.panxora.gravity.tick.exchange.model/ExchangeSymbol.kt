package com.panxora.gravity.tick.exchange.model

import java.io.Serializable

data class ExchangeSymbol(
        val exchange: String, val symbol: String
) : Serializable