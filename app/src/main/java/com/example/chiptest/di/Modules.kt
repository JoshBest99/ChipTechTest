package com.example.chiptest.di

import com.example.chiptest.ui.dog_details.DogDetailsViewModel
import com.example.chiptest.ui.dog_list.DogListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { DogListViewModel(get()) }
    viewModel { DogDetailsViewModel(get(), get()) }
}

val repoModules = module {
    single { provideApiRepository(get()) }
    single { provideGetDogsAsListUseCase(get()) }
}