package com.example.chiptest.data

import com.example.chiptest.model.DogModel

class ApiRepository(private val apiService: ApiService) {
    suspend fun getDogs(): DogModel = apiService.getDogs()

    suspend fun getDogImages(dog: String): List<String> = apiService.getDogImages(dog = dog).images
}