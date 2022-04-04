package com.panxora.gravity.tick.exchange.bitfinex;

import java.math.BigDecimal;

public class BookPrice {

    private final String[] packet;

    public BookPrice(final String packet) {
        this.packet = packet.replace("[", "").replace("]", "").split(",");
    }

    public BigDecimal getPrice() {
        return new BigDecimal(packet[1]);
    }

    public boolean isBid() {
        double v = Double.parseDouble(packet[3]);
        return v > 0;
    }
}
