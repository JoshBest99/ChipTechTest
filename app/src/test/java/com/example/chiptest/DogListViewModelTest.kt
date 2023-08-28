package com.example.chiptest

import com.example.chiptest.domain.GetDogsAsListUseCase
import com.example.chiptest.ui.dog_list.DogListViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class DogListViewModelTest {
    @Mock
    private lateinit var getDogsAsListUseCase: GetDogsAsListUseCase
    private lateinit var dogListViewModel: DogListViewModel

    @Before
    fun setUp() {
        //Force the code to run synchronously to stop race conditions
        Dispatchers.setMain(Dispatchers.Unconfined)
        //Init Mocks
        MockitoAnnotations.openMocks(this)
        dogListViewModel = DogListViewModel(getDogsAsListUseCase)
    }

    @Test
    fun `test getDogsAsListUseCase success`() = runBlocking {
        val dogs = listOf("Affenpinscher", "African", "Airedale")

        //Arrange
        Mockito.`when`(getDogsAsListUseCase()).thenReturn(dogs)

        //Call
        dogListViewModel.getDogList()

        //Assert
        assertEquals(dogs, dogListViewModel.dogListUiState.value.dogs)
        assertEquals(false, dogListViewModel.dogListUiState.value.isRefreshing)
        assertEquals(null, dogListViewModel.dogListUiState.value.error)
    }

    @Test
    fun `test getDogsAsListUseCase throwing error`() = runBlocking {
        //Arrange
        Mockito.`when`(getDogsAsListUseCase()).thenThrow(RuntimeException("Error"))

        //Call
        dogListViewModel.getDogList()

        //Assert
        assertEquals(emptyList<String>(), dogListViewModel.dogListUiState.value.dogs)
        assertEquals("Oops, Something went wrong!", dogListViewModel.dogListUiState.value.error)
    }

}