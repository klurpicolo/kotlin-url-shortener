package io.klur.tutorial.urlshortener

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BlockingUrlShortenerApplication

fun main(args: Array<String>) {
    runApplication<BlockingUrlShortenerApplication>(*args)
}
