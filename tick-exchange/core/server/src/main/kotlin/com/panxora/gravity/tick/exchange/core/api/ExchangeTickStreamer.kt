package com.panxora.gravity.tick.exchange.core.api

import com.panxora.gravity.tick.exchange.core.Symbol

interface ExchangeTickStreamer {
    fun connect()
    fun subscribe(symbols: List<Symbol>)
}