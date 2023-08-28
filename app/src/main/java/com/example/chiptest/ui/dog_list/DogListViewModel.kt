package com.example.chiptest.ui.dog_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chiptest.domain.GetDogsAsListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException

class DogListViewModel(private val getDogsAsListUseCase: GetDogsAsListUseCase) : ViewModel() {

    private val _dogListUiState = MutableStateFlow(DogListUiState())
    val dogListUiState: StateFlow<DogListUiState> = _dogListUiState.asStateFlow()

    init {
        getDogList()
    }

    fun getDogList() {
        _dogListUiState.update {
            it.copy(isRefreshing = true)
        }
        viewModelScope.launch {
            try {
                val dogList = getDogsAsListUseCase()
                _dogListUiState.update {
                    it.copy(dogs = dogList, isRefreshing = false, error = null)
                }
            } catch (exception: Exception) {
                //If the API returned an error body we could display that
                //We could also display different messages for a lot of different errors.
                when (exception) {
                    //Log an event
                    is UnknownHostException,
                    is HttpException -> { //On this one we could display different messages depending on the code.
                        _dogListUiState.update {
                            it.copy(error = "There seems to be a connection issue\nPlease try again later!", isRefreshing = false)
                        }
                    }

                    else -> {
                        _dogListUiState.update {
                            it.copy(error = "Oops, Something went wrong!", isRefreshing = false)
                        }
                    }
                }

            }
        }
    }

}

data class DogListUiState(
    val dogs: List<String> = listOf(),
    val isRefreshing: Boolean = false,
    val error: String? = null
)

