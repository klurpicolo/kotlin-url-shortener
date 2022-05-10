package io.klur.tutorial.urlshortener

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
class AppConfiguration {

    @Value("\${spring.redis.host}")
    lateinit var redisHost: String

    @Value("\${spring.redis.port}")
    lateinit var redisPort: String

    @Bean
    @ConditionalOnProperty(name = ["app.cache.type"], havingValue = "redis")
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(redisHost, redisPort.toInt())
    }

    @Bean
    @ConditionalOnProperty(name = ["app.cache.type"], havingValue = "redis")
    fun redisTemplate(): RedisTemplate<String, String> {
        val template = RedisTemplate<String, String>()
        template.setConnectionFactory(redisConnectionFactory())
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = StringRedisSerializer()
        return template
    }

    @Bean
    @ConditionalOnProperty(name = ["app.cache.type"], havingValue = "redis")
    fun appRedisCache(redisTemplate: RedisTemplate<String, String>): UrlCache {
        return RedisCache(redisTemplate)
    }

    @Bean
    @ConditionalOnProperty(name = ["app.cache.type"], havingValue = "hashMap")
    fun appHashMapCache(): UrlCache {
        return HashMapCache(hashMapOf())
    }
}