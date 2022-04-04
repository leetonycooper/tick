package com.panxora.gravity.tick.exchange.core

import com.panxora.gravity.tick.websocket.model.PriceRequest
import com.panxora.gravity.tick.websocket.subscriber.PriceRequestHandler
import com.panxora.gravity.tick.exchange.core.api.SynchronousExchangeTickPublisher
import com.panxora.gravity.tick.exchange.model.ExchangeSymbol
import com.panxora.gravity.tick.exchange.model.TickChangeAttribution
import com.panxora.gravity.tick.exchange.model.TickSource
import com.panxora.gravity.tick.exchange.publisher.PriceEventPublisher
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class PriceRequestHandlerImpl(
        private val symbolRespository: SymbolRespository,
        private val synchronousExchangeTick: SynchronousExchangeTickPublisher,
        private val priceEventPublisher: PriceEventPublisher,
        private val cache: ExchangeTickCache,
        @Value("\${com.panxora.gravity.tick.symbol.groupId:1}")
        val groupId: Int
) : PriceRequestHandler {
    override fun handle(priceRequest: PriceRequest) {
        if(priceRequest.exchange == synchronousExchangeTick.getExchange()) {
            val cachedTick = cache.get(exchangeSymbol = ExchangeSymbol(exchange = priceRequest.exchange, symbol = priceRequest.symbol))
            if(cachedTick != null){
                priceEventPublisher.publish(cachedTick.copy(
                    changeAttributedTo = TickChangeAttribution.ON_PRICE_REQUEST,
                    source = TickSource.EXCHANGE_CACHE
                ))
            }
            else {
                synchronousExchangeTick.publishTicksForSymbols(
                    symbolRespository
                        .findByFeedGroupIdAndExchangeName(groupId, synchronousExchangeTick.getExchange())
                            .filter { it.tradePair == priceRequest.symbol },
                    TickChangeAttribution.ON_PRICE_REQUEST
                )
            }
        }
    }
}