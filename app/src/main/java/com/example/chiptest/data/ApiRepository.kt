package com.example.chiptest.data

import com.example.chiptest.model.DogModel

class ApiRepository(private val apiService: ApiService) {
    suspend fun getDogs(): DogModel = apiService.getDogs()
}