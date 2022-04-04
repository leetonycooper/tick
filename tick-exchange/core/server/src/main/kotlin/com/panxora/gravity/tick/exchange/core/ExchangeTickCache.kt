package com.panxora.gravity.tick.exchange.core

import com.panxora.gravity.tick.exchange.model.*
import org.springframework.stereotype.Component
import java.lang.RuntimeException
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Component
class ExchangeTickCache {
    private val ticks = ConcurrentHashMap<ExchangeSymbol, ExchangeTickEvent>()

    fun get(exchangeSymbol: ExchangeSymbol) : ExchangeTickEvent?{
        return ticks[exchangeSymbol]
    }

    fun put(exchangeSymbol: ExchangeSymbol, exchangeTick: ExchangeTickEvent){
        ticks[exchangeSymbol] = exchangeTick
    }

    fun update(exchangeSymbol: ExchangeSymbol, price: Price) : ExchangeTickEvent{
        val ticker = ticks[exchangeSymbol] ?: throw RuntimeException("$exchangeSymbol ticker not found in cache")

        val newTicker = when(price){
            is Bid -> ticker.copy(
                    bid = price,
                    changeAttributedTo = TickChangeAttribution.BID_UPDATE,
                    source = TickSource.EXCHANGE_STREAM,
                    timestamp = price.timestamp,
                    eventId = UUID.randomUUID().toString()
            )
            is Ask -> ticker.copy(
                    ask = price,
                    changeAttributedTo = TickChangeAttribution.ASK_UPDATE,
                    source = TickSource.EXCHANGE_STREAM,
                    timestamp = price.timestamp,
                    eventId = UUID.randomUUID().toString()
            )
            is Last -> ticker.copy(
                    last = price,
                    changeAttributedTo = TickChangeAttribution.LAST_UPDATE,
                    source = TickSource.EXCHANGE_STREAM,
                    timestamp = price.timestamp,
                    eventId = UUID.randomUUID().toString()
            )

            else -> throw RuntimeException("Unsupported price type ${price.javaClass.canonicalName}")
        }

        ticks[exchangeSymbol] = newTicker
        return newTicker
    }
}