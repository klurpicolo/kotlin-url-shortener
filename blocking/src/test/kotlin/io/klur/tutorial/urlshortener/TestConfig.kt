package io.klur.tutorial.urlshortener

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestConfig {

    @Bean
    fun restTemplate(): TestRestTemplate {
        return TestRestTemplate()
    }
}