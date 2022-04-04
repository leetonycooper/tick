package com.panxora.gravity.tick.exchange.core

import com.panxora.gravity.tick.exchange.model.ExchangeSymbol
import com.panxora.gravity.tick.exchange.model.ExchangeTickEvent
import com.panxora.gravity.tick.exchange.model.Price
import com.panxora.gravity.tick.exchange.publisher.PriceEventPublisher
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class CoreExchangeTickPublisher(
        private val publisher: PriceEventPublisher,
        private val cache: ExchangeTickCache
) : HealthIndicator {

    private var lastPriceToBePublished: Instant = Instant.now()

    fun publish(tick : ExchangeTickEvent){
        lastPriceToBePublished = Instant.now()
        cache.put(exchangeSymbol = tick.exchangeSymbol, exchangeTick = tick)
        publisher.publish(tick)
    }

    fun publish(
            exchangeSymbol: ExchangeSymbol,
            price : Price){
        lastPriceToBePublished = Instant.now()
        publisher.publish(cache.update(exchangeSymbol, price))
    }

    override fun health(): Health {
        return if(notSeenAPriceWithinConfiguredTime()){
            Health.down().build()
        }
        else{
            Health.up().build()
        }
    }

    private fun notSeenAPriceWithinConfiguredTime() =
            lastPriceToBePublished.isBefore(Instant.now().minusSeconds(300))
}