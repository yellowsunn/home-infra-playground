package com.yellowsunn.example.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Qualifier("dummyJson")
    @Bean
    fun dummyJsonWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("https://dummyjson.com")
            .build()
    }
}
