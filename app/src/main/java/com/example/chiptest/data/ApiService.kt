package com.example.chiptest.data

import com.example.chiptest.model.DogModel
import retrofit2.http.*

interface ApiService {
    @GET("breeds/list/all")
    suspend fun getDogs(): DogModel
}