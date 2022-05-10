package io.klur.tutorial.urlshortener

import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.URL

data class LongUrlReq(
    @field:NotNull
    @field:URL
    val url: String?
)

data class ShortUrlRes(val shortenURL: String)