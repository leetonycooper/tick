package com.panxora.gravity.tick.websocket

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class CacheConfig {
    @Bean
    fun kotlinModule() : Module{
        return KotlinModule()
    }
}