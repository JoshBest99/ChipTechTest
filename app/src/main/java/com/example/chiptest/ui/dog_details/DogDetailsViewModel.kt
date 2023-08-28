package com.example.chiptest.ui.dog_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chiptest.data.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DogDetailsViewModel(private val dog: String, private val apiRepository: ApiRepository) : ViewModel() {

    private val _dogDetailsUiState = MutableStateFlow(DogDetailsUiState())
    val dogDetailsUiState: StateFlow<DogDetailsUiState> = _dogDetailsUiState.asStateFlow()

    init {
        getDogImages()
    }

    fun getDogImages() {
        _dogDetailsUiState.update {
            it.copy(dog = dog, isRefreshing = true)
        }
        viewModelScope.launch {
            try {
                val images = apiRepository.getDogImages(dog)
                _dogDetailsUiState.update {
                    it.copy(images = images, isRefreshing = false, error = null)
                }
            } catch (e: Exception) {
                _dogDetailsUiState.update {
                    it.copy(error = "Oops, Something went wrong!", isRefreshing = false)
                }
            }
        }
    }
}

data class DogDetailsUiState(
    val dog: String = "",
    val images: List<String> = listOf(),
    val isRefreshing: Boolean = false,
    val error: String? = null
)
