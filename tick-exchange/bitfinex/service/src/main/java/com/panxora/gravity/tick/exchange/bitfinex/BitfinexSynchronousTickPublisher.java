package com.panxora.gravity.tick.exchange.bitfinex;

import com.panxora.gravity.tick.exchange.core.CoreExchangeTickPublisher;
import com.panxora.gravity.tick.exchange.core.Symbol;
import com.panxora.gravity.tick.exchange.core.api.SynchronousExchangeTickPublisher;
import com.panxora.gravity.tick.exchange.model.*;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class BitfinexSynchronousTickPublisher implements SynchronousExchangeTickPublisher {

    private final CoreExchangeTickPublisher tickPublisher;
    private final BitfinexRestClient client;
    private final String exchange;

    private BitfinexSynchronousTickPublisher(
            @Autowired CoreExchangeTickPublisher tickPublisher,
            @Autowired BitfinexRestClient client,
            @Value("${tick.exchange.name:bitfinex}") String exchange
    ){
        this.tickPublisher = tickPublisher;
        this.client = client;
        this.exchange = exchange;
    }

    @Override
    public void publishTicksForSymbols(@NotNull List<? extends Symbol> symbols, TickChangeAttribution attribution) {
        symbols.parallelStream()
                .map(s -> new Pair<>(s, client.getTickerForSymbol(s.getTradePair())))
                .forEach( p ->
                    sendPrices(p.getFirst().getTradePair(), p.getSecond(), attribution)
                );
    }

    public void sendPrices(String symbol, BitfinexTicker ticker, TickChangeAttribution attribution){
        Instant now = Instant.now();
        final ExchangeTickEvent exchangeTicker = new ExchangeTickEvent(
                UUID.randomUUID().toString(),
                new ExchangeSymbol(exchange,
                        symbol),
                new Bid(ticker.getBid(), now),
                new Ask(ticker.getAsk(), now),
                new Last(ticker.getLast(), now,null, (Instant.now().getEpochSecond()*1000)+""),
                attribution,
                TickSource.EXCHANGE_REQUEST,
                Instant.now()
        );
        tickPublisher.publish(exchangeTicker);
    }

    @NotNull
    @Override
    public String getExchange() {
        return exchange;
    }
}
