package io.klur.tutorial.urlshortener

import com.google.common.hash.Hashing
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders.LOCATION
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.nio.charset.StandardCharsets

@RestController
class Controller(@Value("\${server.port}")
                 private val port: Int,
                 private val urlCache: UrlCache
) {

    private val log = KotlinLogging.logger {}

    @GetMapping(value = ["/{shortenUrl}"])
    fun get(@PathVariable shortenUrl: String): ResponseEntity<Any> {
        val fullUrl = urlCache.get(shortenUrl)
        if (fullUrl.isNullOrBlank()) {
            log.info("$shortenUrl does not hit cache")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
        log.info("$shortenUrl hits cache")
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
            .header(LOCATION, URI.create(fullUrl).toASCIIString()).build()
    }

    @PostMapping(value = ["/generate"])
    fun generate(@RequestBody @Valid urlReq: LongUrlReq): ResponseEntity<ShortUrlRes> {
        val shortenUrl = Hashing.murmur3_32_fixed().hashString(urlReq.url.orEmpty(), StandardCharsets.UTF_8).toString()
        urlCache.put(shortenUrl, urlReq.url.orEmpty())
        log.info("$shortenUrl is generated from ${urlReq.url}")
        return ResponseEntity.status(HttpStatus.OK)
            .body(ShortUrlRes("localhost:$port/$shortenUrl"))
    }
}
