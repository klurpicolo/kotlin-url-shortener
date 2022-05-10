package io.klur.tutorial.urlshortener

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@SpringBootTest(
    classes = [TestConfig::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class UrlShortenerApplicationTests {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @LocalServerPort
    protected var port: Int = 0

    @Test
    @Order(1)
    fun generate() {
        val res = restTemplate.postForEntity(
            "http://localhost:$port/generate",
            LongUrlReq("https://www.baeldung.com/kotlin/spring-boot-testing"),
            ShortUrlRes::class.java
        )

        assertThat(res.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    @Order(2)
    fun get() {
        val res = restTemplate.getForEntity(
            "http://localhost:$port/invalid",
            String.Companion::class.java
        )
        assertThat(res.statusCode).isEqualTo(HttpStatus.PERMANENT_REDIRECT)
    }

    @Test
    @Order(3)
    fun notGenerateNullUrl() {
        val res = restTemplate.postForEntity(
            "http://localhost:$port/generate",
            LongUrlReq(null),
            Any::class.java
        )
        assertThat(res.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Order(4)
    fun notGenerateInvalidUrl() {
        val res = restTemplate.postForEntity(
            "http://localhost:$port/generate",
            LongUrlReq("invalidUrlInput"),
            String.Companion::class.java
        )
        assertThat(res.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }
}
