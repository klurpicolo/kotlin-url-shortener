package io.klur.tutorial.urlshortener

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializationContext.RedisSerializationContextBuilder
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
class AppConfiguration {

    @Value("\${spring.redis.host}")
    lateinit var redisHost: String

    @Value("\${spring.redis.port}")
    lateinit var redisPort: String

    @Bean
    @ConditionalOnProperty(name= ["app.cache.type"], havingValue="redis")
    fun reactiveRedisTemplate(factory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, String> {
        val builder: RedisSerializationContextBuilder<String, String> =
            RedisSerializationContext.newSerializationContext(StringRedisSerializer())
        val context: RedisSerializationContext<String, String> =
            builder.value(StringRedisSerializer()).build()
        return ReactiveRedisTemplate(factory, context)
    }

    @Bean
    @ConditionalOnProperty(name= ["app.cache.type"], havingValue="hashMap")
    fun appHashMapCache(): UrlCache {
        return HashMapCache(hashMapOf())
    }

    @Bean
    @ConditionalOnProperty(name= ["app.cache.type"], havingValue="redis")
    fun appRedisCache(reactiveRedisTemplate: ReactiveRedisTemplate<String, String>): UrlCache {
        return RedisCache(reactiveRedisTemplate.opsForValue())
    }
}