package com.example.chiptest.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DogImageModel(
    @Json(name = "message")
    val images: List<String>
)