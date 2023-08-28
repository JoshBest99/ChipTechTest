package com.example.chiptest.domain

import com.example.chiptest.data.ApiRepository

class GetDogsAsListUseCase(private val apiRepository: ApiRepository) {

    //We could potentially add a retry function here, whilst checking network connectivity.
    suspend operator fun invoke(): List<String> {
        //Retrofit will automatically handle changing over to the IO thread here, so theres no need to wrap it.
        val dogsResponse = apiRepository.getDogs()
        //Converting to a List so we preserve the original order of the dogs.
        return dogsResponse.dogs.keys.toList()
    }

}