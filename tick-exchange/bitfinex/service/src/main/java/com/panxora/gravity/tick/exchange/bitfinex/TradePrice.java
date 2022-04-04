package com.panxora.gravity.tick.exchange.bitfinex;

import java.math.BigDecimal;

public class TradePrice {

    private String[] packet;

    public TradePrice(final String packet) {
        this.packet = packet.replace("[", "").replace("]", "").split(",");
    }

    public BigDecimal getLast() {
        return new BigDecimal(packet[5]);
    }

    public String getTimestamp() {
        return packet[3];
    }

    public String getTradeId() {
        return packet[2];
    }

}