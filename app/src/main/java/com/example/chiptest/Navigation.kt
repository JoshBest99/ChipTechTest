package com.example.chiptest

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chiptest.ui.dog_details.DogDetails
import com.example.chiptest.ui.dog_details.DogDetailsViewModel
import com.example.chiptest.ui.dog_list.DogList
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NavigationSetup(
    navController: NavHostController,
    startDestination: String,
) {
    NavHost(navController, startDestination = startDestination) {
        composable(Screen.DogList.route) {
            DogList {
                navController.navigate(it)
            }
        }
        composable(Screen.DogDetails.route) {
            it.arguments?.getString("dogType")?.let { dogType ->
                val viewModel: DogDetailsViewModel = koinViewModel(parameters = { parametersOf(dogType) })
                DogDetails(viewModel)
            }
        }
    }
}

sealed class Screen(val route: String, val routeWithArgs: String?) {
    object DogList : Screen("dog_list", null)
    object DogDetails : Screen("dog_details/{dogType}", "dog_details")
}