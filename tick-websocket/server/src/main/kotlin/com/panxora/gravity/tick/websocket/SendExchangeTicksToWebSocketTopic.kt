package com.panxora.gravity.tick.websocket

import com.panxora.gravity.tick.exchange.model.ExchangeCloseEvent
import com.panxora.gravity.tick.exchange.model.ExchangeSymbol
import com.panxora.gravity.tick.exchange.model.ExchangeTickEvent
import com.panxora.gravity.tick.exchange.model.PriceEvent
import com.panxora.gravity.tick.exchange.subscriber.PriceEventHandler
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class SendExchangeTicksToWebSocketTopic(
    private val template: SimpMessagingTemplate
) : PriceEventHandler{

    private val latestTicks = ConcurrentHashMap<ExchangeSymbol, ExchangeTickEvent>()

    override fun handle(priceEvent: PriceEvent) {
        when(priceEvent){
            is ExchangeTickEvent -> handleExchangeTick(priceEvent)
            is ExchangeCloseEvent -> handleExchangeClose(priceEvent)
        }
    }

    private fun handleExchangeTick(tick : ExchangeTickEvent) {
        val latestTick = latestTicks[tick.exchangeSymbol]
        if (latestTick != null && latestTick.timestamp.isAfter(tick.timestamp)) {
            println("WARN - seen old price $tick trying to order write newer price $latestTick")
        } else {
            latestTicks[tick.exchangeSymbol] = tick
            template.convertAndSend("/topic/${tick.exchangeSymbol.exchange}/${tick.exchangeSymbol.symbol}", ExchangeTick(
                    exchange = tick.exchangeSymbol.exchange,
                    symbol = tick.exchangeSymbol.symbol,
                    ask = tick.ask.price,
                    bid = tick.bid.price,
                    last = tick.last.price,
                    changeAttributedTo = tick.changeAttributedTo,
                    eventId = tick.eventId
            ))
        }
    }

    private fun handleExchangeClose(close : ExchangeCloseEvent) {
        template.convertAndSend("/topic/${close.exchangeSymbol.exchange}/${close.exchangeSymbol.symbol}", ExchangeClose(
                exchange = close.exchangeSymbol.exchange,
                symbol = close.exchangeSymbol.symbol,
                close = close.close.price,
                closeDate = close.close.closeDate,
                eventId = close.eventId
        ))
    }
}