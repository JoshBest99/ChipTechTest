package com.example.chiptest.ui.dog_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chiptest.domain.GetDogsAsListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DogListViewModel(private val getDogsAsListUseCase: GetDogsAsListUseCase) : ViewModel() {

    private val _dogListUiState = MutableStateFlow(DogListUiState())
    val dogListUiState: StateFlow<DogListUiState> = _dogListUiState.asStateFlow()

    init {
        getDogList()
    }

    fun getDogList() {
        viewModelScope.launch {
            val dogList = getDogsAsListUseCase.invoke()
            _dogListUiState.update {
                it.copy(dogs = dogList)
            }
        }
    }
}

data class DogListUiState(
    val dogs: List<String> = listOf(),
    val isRefreshing: Boolean = false,
    val error: String? = null
)

