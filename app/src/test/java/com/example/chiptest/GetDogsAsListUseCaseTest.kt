package com.example.chiptest

import com.example.chiptest.data.ApiRepository
import com.example.chiptest.domain.GetDogsAsListUseCase
import com.example.chiptest.model.DogModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class GetDogsAsListUseCaseTest {

    @Mock
    private lateinit var apiRepository: ApiRepository

    private lateinit var getDogsAsListUseCase: GetDogsAsListUseCase


    @Before
    fun setUp() {
        //Init Mocks
        MockitoAnnotations.openMocks(this)
        getDogsAsListUseCase = GetDogsAsListUseCase(apiRepository)
    }

    @Test
    fun `test getDogsAsListUseCase converts to a list of strings`() = runBlocking {
        // Sample JSON response
        val jsonResponse = """
            {
                "message": {
                    "affenpinscher": [""],
                    "african": [],
                    "bulldog": [
                       "boston",
                       "english",
                       "french"
                    ]
                }
            }
        """

        //Mocking API response
        val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val jsonAdapter: JsonAdapter<DogModel> = moshi.adapter(DogModel::class.java)
        val mockedResponse = jsonAdapter.fromJson(jsonResponse)

        //Arrange
        `when`(apiRepository.getDogs()).thenReturn(mockedResponse)

        //Call
        val result = getDogsAsListUseCase()

        //Assert
        val expectedBreeds = listOf("affenpinscher", "african", "bulldog")
        assertEquals(expectedBreeds, result)
    }

}