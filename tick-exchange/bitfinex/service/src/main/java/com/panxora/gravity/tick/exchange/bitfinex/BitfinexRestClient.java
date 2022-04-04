package com.panxora.gravity.tick.exchange.bitfinex;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class BitfinexRestClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public BitfinexTicker getTickerForSymbol(@NotNull String symbol) {
        return new BitfinexTicker(
            Objects
                .requireNonNull(restTemplate.getForObject("https://api-pub.bitfinex.com/v2/ticker/t" + symbol,
                String.class)
            )
        );
    }
}
