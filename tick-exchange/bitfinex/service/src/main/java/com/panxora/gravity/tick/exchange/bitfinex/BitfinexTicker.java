package com.panxora.gravity.tick.exchange.bitfinex;

import java.math.BigDecimal;
import java.util.Arrays;

public class BitfinexTicker {
    private final String[] packet;

    public BitfinexTicker(
        final String packet
    ) {
        this.packet = packet.replace("[", "").replace("]", "").split(",");
    }

    public BigDecimal getBid(){
        return new BigDecimal(packet[0]);
    }

    public BigDecimal getAsk(){
        return new BigDecimal(packet[2]);
    }

    public BigDecimal getLast(){
        return new BigDecimal(packet[6]);
    }

    @Override
    public String toString() {
        return "Ticker{" +
                "packet=" + Arrays.toString(packet) +
                '}';
    }
}