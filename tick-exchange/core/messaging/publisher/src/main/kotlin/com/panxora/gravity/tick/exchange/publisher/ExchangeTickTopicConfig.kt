package com.panxora.gravity.tick.exchange.publisher

import org.apache.activemq.command.ActiveMQTopic
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.jms.Topic

@Configuration
class ExchangeTickTopicConfig {
    @Bean
    @Qualifier("EXCHANGE_PRICES")
    fun exchangePricesTopic(): Topic {
        return ActiveMQTopic("EXCHANGE_PRICES")
    }
}