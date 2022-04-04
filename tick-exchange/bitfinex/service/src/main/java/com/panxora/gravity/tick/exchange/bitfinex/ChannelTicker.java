package com.panxora.gravity.tick.exchange.bitfinex;

import java.math.BigDecimal;

public class ChannelTicker {
    private final String[] packet;

    public ChannelTicker(
        final String packet
    ) {
        this.packet = packet.replace("[", "").replace("]", "").split(",");
    }

    public Integer getChannelId(){
        return Integer.valueOf(packet[0]);
    }

    public BigDecimal getBid(){
        return new BigDecimal(packet[1]);
    }

    public BigDecimal getAsk(){
        return new BigDecimal(packet[3]);
    }

    public BigDecimal getLast(){
        return new BigDecimal(packet[7]);
    }
}