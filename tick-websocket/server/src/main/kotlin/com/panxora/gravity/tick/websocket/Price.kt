package com.panxora.gravity.tick.websocket

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes(
        JsonSubTypes.Type(value = ExchangeTick::class, name = "exchange-tick"),
        JsonSubTypes.Type(value = ExchangeClose::class, name = "exchange-close")
)
interface Price