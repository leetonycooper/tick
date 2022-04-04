package com.panxora.gravity.tick.exchange.core

import com.panxora.gravity.tick.exchange.core.api.SynchronousExchangeTickPublisher
import com.panxora.gravity.tick.exchange.core.api.ExchangeTickStreamer
import com.panxora.gravity.tick.exchange.model.TickChangeAttribution
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class StartTickers(
        val exchangeTickStreamer: ExchangeTickStreamer,
        val symbolRespository: SymbolRespository,
        val synchronousExchangeTickPublisher: SynchronousExchangeTickPublisher,
        @Value("\${com.panxora.gravity.tick.symbol.groupId:1}")
        val groupId: Int
) {

    @EventListener
    fun startTickers(referContextRefreshedEvent: ContextRefreshedEvent){
        val symbols = symbolRespository.findByFeedGroupIdAndExchangeName(groupId, synchronousExchangeTickPublisher.getExchange())
        synchronousExchangeTickPublisher.publishTicksForSymbols(symbols, TickChangeAttribution.INITIAL_SEEDING)
        exchangeTickStreamer.connect()
        exchangeTickStreamer.subscribe(symbols)
    }
}