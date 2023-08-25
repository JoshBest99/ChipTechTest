package com.example.chiptest.ui.dog_list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DogListViewModel : ViewModel() {

    private val _dogListUiState = MutableStateFlow(DogListUiState())
    val dogListUiState: StateFlow<DogListUiState> = _dogListUiState.asStateFlow()

}

data class DogListUiState(
    val dogs: List<String> = listOf(),
    val isRefreshing: Boolean = false,
    val error: String? = null
)

