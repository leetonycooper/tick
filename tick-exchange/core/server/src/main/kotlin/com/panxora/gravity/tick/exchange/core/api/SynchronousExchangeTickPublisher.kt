package com.panxora.gravity.tick.exchange.core.api

import com.panxora.gravity.tick.exchange.core.Symbol
import com.panxora.gravity.tick.exchange.model.TickChangeAttribution

interface SynchronousExchangeTickPublisher {
    fun publishTicksForSymbols(symbols: List<Symbol>, attribution: TickChangeAttribution)
    fun getExchange() : String
}