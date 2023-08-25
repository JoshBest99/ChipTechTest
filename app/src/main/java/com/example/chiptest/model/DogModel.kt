package com.example.chiptest.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DogModel(
    @Json(name = "message")
    val dogs: Map<String, List<String>>
)