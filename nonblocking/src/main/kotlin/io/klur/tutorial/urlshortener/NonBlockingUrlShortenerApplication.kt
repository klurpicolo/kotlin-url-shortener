package io.klur.tutorial.urlshortener

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NonBlockingUrlShortenerApplication

fun main(args: Array<String>) {
	runApplication<NonBlockingUrlShortenerApplication>(*args)
}