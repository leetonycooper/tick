package com.panxora.gravity.tick.websocket.model

import java.io.Serializable

data class PriceRequest(
    val exchange: String, val symbol: String
) : Serializable