package com.example.chiptest

import com.example.chiptest.data.ApiRepository
import com.example.chiptest.ui.dog_details.DogDetailsViewModel
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class DogDetailsViewModelTest {
    @Mock
    private lateinit var apiRepository: ApiRepository
    private lateinit var dogDetailsViewModel: DogDetailsViewModel

    @Before
    fun setUp() {
        //Force the code to run synchronously to stop race conditions
        Dispatchers.setMain(Dispatchers.Unconfined)
        //Init Mocks
        MockitoAnnotations.openMocks(this)
        dogDetailsViewModel = DogDetailsViewModel("dog", apiRepository)
    }

    @Test
    fun `test getDogImages success`() = runBlocking {
        val images = listOf("image 1", "image 2")

        //Arrange
        Mockito.`when`(apiRepository.getDogImages("dog")).thenReturn(images)

        //Call
        dogDetailsViewModel.getDogImages()

        //Assert
        TestCase.assertEquals(images, dogDetailsViewModel.dogDetailsUiState.value.images)
        TestCase.assertEquals(false, dogDetailsViewModel.dogDetailsUiState.value.isRefreshing)
        TestCase.assertEquals(null, dogDetailsViewModel.dogDetailsUiState.value.error)
    }

    @Test
    fun `test getDogImages throwing error`() = runBlocking {
        //Arrange
        Mockito.`when`(apiRepository.getDogImages("dog")).thenThrow(RuntimeException("Error"))

        //Call
        dogDetailsViewModel.getDogImages()

        //Assert
        TestCase.assertEquals(emptyList<String>(), dogDetailsViewModel.dogDetailsUiState.value.images)
        TestCase.assertEquals("Oops, Something went wrong!", dogDetailsViewModel.dogDetailsUiState.value.error)
    }


}