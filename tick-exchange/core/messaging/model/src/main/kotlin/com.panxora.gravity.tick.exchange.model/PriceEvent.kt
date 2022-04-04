package com.panxora.gravity.tick.exchange.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = ExchangeTickEvent::class, name = "exchange-tick"),
    JsonSubTypes.Type(value = ExchangeCloseEvent::class, name = "exchange-close")
)
interface PriceEvent : Event{
    val exchangeSymbol: ExchangeSymbol
}