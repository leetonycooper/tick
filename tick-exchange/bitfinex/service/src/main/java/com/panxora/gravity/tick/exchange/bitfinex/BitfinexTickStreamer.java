package com.panxora.gravity.tick.exchange.bitfinex;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.panxora.gravity.tick.exchange.core.CoreExchangeTickPublisher;
import com.panxora.gravity.tick.exchange.core.Symbol;
import com.panxora.gravity.tick.exchange.core.api.ExchangeTickStreamer;
import com.panxora.gravity.tick.exchange.model.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ClientEndpoint
public class BitfinexTickStreamer implements ExchangeTickStreamer {

    private ObjectMapper objectMapper;
    private CoreExchangeTickPublisher tickPublisher;
    private Session session;
    private final Map<Integer, MessageChannel> channels = new ConcurrentHashMap<>();
    private final String exchange;

    public BitfinexTickStreamer(
            @Autowired ObjectMapper objectMapper,
            @Autowired CoreExchangeTickPublisher tickPublisher,
            @Value("${tick.exchange.name:bitfinex}") String exchange
    ){
        this.tickPublisher = tickPublisher;
        this.objectMapper = objectMapper;
        this.exchange = exchange;
    }

    @Override
    public void connect() {
        final WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        try {
            session = container.connectToServer(this, new URI("wss://api.bitfinex.com/ws/2"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    @NotNull
    private List<String> getSubscriptionRequest(String symbol) {
        final String ticker = "{\n" +
                "   \"event\":\"subscribe\",\n" +
                "   \"channel\":\"ticker\",\n" +
                "   \"pair\":\"t"+symbol+"\"\n" +
                "}";
        final String trades = "{\n" +
                "   \"event\":\"subscribe\",\n" +
                "   \"channel\":\"trades\",\n" +
                "   \"pair\":\"t"+symbol+"\"\n" +
                "}";
        final String book = "{\n" +
                "   \"event\":\"subscribe\",\n" +
                "   \"channel\":\"book\",\n" +
                "   \"pair\":\"t"+symbol+"\"\n" +
                "}";

        final ArrayList<String> subscription = new ArrayList<>();
        //subscription.add(ticker);
        subscription.add(trades);
        subscription.add(book);
        return subscription;
    }

    @OnMessage
    public void onMessage(String message, Session session) {

        Instant now = Instant.now();
        try {
            if (message.contains("chanId")) {
                Channel channel = objectMapper.readValue(message, Channel.class);
                channels.put(
                        channel.getChanId(),
                        new MessageChannel(getType(channel.getChannel()), channel.getSymbol()));
            }
            if (!message.contains("hb") && !message.contains("event")) {
                final String channelId =
                        message.replace("[", "").replace("]", "").split(",")[0];
                final MessageChannel messageChannel = channels.get(Integer.parseInt(channelId));
                if(messageChannel.type == ChannelType.TICKER) {
                    sendPrices(new ChannelTicker(message));
                }
                else if(messageChannel.type == ChannelType.BOOK){
                    BookPrice bookPrice = new BookPrice(message);

                    if (bookPrice.isBid()) {
                        tickPublisher.publish(
                                new ExchangeSymbol(exchange, messageChannel.symbol.substring(1)),
                                new Bid(bookPrice.getPrice(), now)
                        );
                    }
                    else {
                        tickPublisher.publish(
                                new ExchangeSymbol(exchange, messageChannel.symbol.substring(1)),
                                new Ask(bookPrice.getPrice(), now)
                        );
                    }
                }
                else if(messageChannel.type == ChannelType.TRADES){
                    if (message.contains("te")) {
                        TradePrice tradePrice = new TradePrice(message);
                        tickPublisher.publish(
                                new ExchangeSymbol(exchange, messageChannel.symbol.substring(1)),
                                new Last(
                                        tradePrice.getLast(),
                                        now,
                                        tradePrice.getTradeId(),
                                        tradePrice.getTimestamp()
                                )
                        );
                    }
                }
            }
        }
        catch(Throwable t){
            t.printStackTrace();
        }
    }


    private ChannelType getType(String channel) {
        if ("trades".equals(channel)) {
            return ChannelType.TRADES;
        }

        if ("book".equals(channel)) {
            return ChannelType.BOOK;
        }

        if("ticker".equals(channel)){
            return ChannelType.TICKER;
        }

        throw new RuntimeException("Unknown Type " + channel);
    }

    public void sendPrices(ChannelTicker channelTicker){
        Instant now = Instant.now();
        tickPublisher.publish(
                new ExchangeTickEvent(
                        UUID.randomUUID().toString(),
                        new ExchangeSymbol(
                                exchange,
                                channels.get(channelTicker.getChannelId()).symbol),
                        new Bid(channelTicker.getBid(), now),
                        new Ask(channelTicker.getAsk(), now),
                        new Last(channelTicker.getLast(), now, null,(Instant.now().getEpochSecond()*1000)+""),
                        TickChangeAttribution.INITIAL_SEEDING,
                        TickSource.EXCHANGE_STREAM,
                        now
                )
        );
    }

    @Override
    public void subscribe(@NotNull List<? extends Symbol> symbols) {
        symbols.
                stream()
                .forEach(es -> {
                    getSubscriptionRequest(es.getTradePair()).forEach(subscription ->
                        this.session.getAsyncRemote().sendText(subscription)
                    );
                }
        );
    }

    private static class MessageChannel {

        private ChannelType type;
        private String symbol;

        public MessageChannel(ChannelType type, String symbol) {
            this.type = type;
            this.symbol = symbol;
        }

        public ChannelType getType() {
            return type;
        }

        public String getSymbol() {
            return symbol;
        }
    }
}
