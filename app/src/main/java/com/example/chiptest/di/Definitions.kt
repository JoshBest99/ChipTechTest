package com.example.chiptest.di

import com.example.chiptest.data.ApiRepository
import com.example.chiptest.data.ApiService
import com.example.chiptest.domain.GetDogsAsListUseCase

fun provideApiRepository(apiService: ApiService): ApiRepository = ApiRepository(apiService)

fun provideGetDogsAsListUseCase(apiRepository: ApiRepository) = GetDogsAsListUseCase(apiRepository)