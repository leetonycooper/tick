package com.panxora.gravity.tick.websocket.publisher

import org.apache.activemq.command.ActiveMQTopic
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.jms.Topic

@Configuration
class TickCacheTopicConfig {
    @Bean
    @Qualifier("PRICE_REQUEST")
    fun priceRequestTopic(): Topic {
        return ActiveMQTopic("PRICE_REQUEST")
    }
}