package io.klur.tutorial.urlshortener

import org.springframework.data.redis.core.RedisTemplate

interface UrlCache {
    fun get(key: String): String?
    fun put(key: String, value: String)
}

class HashMapCache(private val cache: MutableMap<String, String>): UrlCache {

    override fun get(key: String): String? {
        return cache[key]
    }

    override fun put(key: String, value: String) {
        cache[key] = value
    }
}

class RedisCache(private val redisTemplate: RedisTemplate<String, String>): UrlCache {

    override fun get(key: String): String? {
        return redisTemplate.opsForValue().get(key)
    }

    override fun put(key: String, value: String) {
        redisTemplate.opsForValue().set(key, value)
    }
}