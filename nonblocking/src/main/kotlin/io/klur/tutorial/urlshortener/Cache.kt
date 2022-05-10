package io.klur.tutorial.urlshortener

import org.springframework.data.redis.core.ReactiveValueOperations
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

interface UrlCache {
    fun get(key: String): Mono<String?>
    fun put(key: String, value: String): Mono<Unit>
}

class HashMapCache(private val cache: MutableMap<String, String>) : UrlCache {

    override fun get(key: String): Mono<String?> {
        return Mono.justOrEmpty(cache[key])
    }

    override fun put(key: String, value: String): Mono<Unit> {
        val result = cache.put(key, value)
        return result.toMono().map { }
    }
}

class RedisCache(private val ops: ReactiveValueOperations<String, String>) : UrlCache {

    override fun get(key: String): Mono<String?> {
        return ops.get(key)
    }

    override fun put(key: String, value: String): Mono<Unit> {
        return ops.set(key, value).map { }
    }
}