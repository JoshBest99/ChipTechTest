package com.example.chiptest.ui.dog_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import com.example.chiptest.R
import com.example.chiptest.Screen
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.compose.koinViewModel

@Composable
fun DogList(viewModel: DogListViewModel = koinViewModel(), navigateTo: (String) -> Unit) {
    val dogsListUiState by viewModel.dogListUiState.collectAsState()

    DogList(dogsListUiState = dogsListUiState, dogClicked = { dogType ->
        Screen.DogDetails.routeWithArgs?.plus("/$dogType")?.let {
            navigateTo(it)
        }
    }, refreshDogs = {
        viewModel.getDogList()
    })

}

@Composable
private fun DogList(dogsListUiState: DogListUiState, dogClicked: (String) -> Unit, refreshDogs: () -> Unit) {
    Column(
        Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(visible = dogsListUiState.error != null) {
            Column(
                verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "${dogsListUiState.error}", style = MaterialTheme.typography.h4.copy(color = Color.Red), textAlign = TextAlign.Center)
                if (!dogsListUiState.isRefreshing) {
                    Button(onClick = { refreshDogs() }) {
                        Text(text = "Try Again", style = MaterialTheme.typography.h5)
                    }
                }
            }
        }

        AnimatedVisibility(visible = dogsListUiState.isRefreshing) {
            CircularProgressIndicator(modifier = Modifier.size(dimensionResource(id = R.dimen.loading_icon_size)))
        }

        AnimatedVisibility(visible = dogsListUiState.error == null && !dogsListUiState.isRefreshing) {
            SwipeRefresh(modifier = Modifier.fillMaxSize(), state = rememberSwipeRefreshState(isRefreshing = dogsListUiState.isRefreshing), onRefresh = { refreshDogs() }) {
                DogListColumn(dogs = dogsListUiState.dogs, dogClicked = dogClicked)
            }
        }
    }
}

@Composable
private fun DogListColumn(dogs: List<String>, dogClicked: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.small_spacing))) {
        items(items = dogs) { dog ->
            DogColumnItem(dog = dog) {
                dogClicked(dog)
            }
        }
    }
}

@Composable
private fun DogColumnItem(dog: String, dogClicked: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.small_spacing))
            .clickable {
                dogClicked()
            },
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.card_rounding)),
        elevation = dimensionResource(id = R.dimen.extra_small_spacing)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.small_spacing))
                .height(dimensionResource(id = R.dimen.dog_item_height)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.medium_spacing))
        ) {
            Image(
                imageVector = Icons.Filled.Pets, contentDescription = dog, modifier = Modifier
                    .size(dimensionResource(id = R.dimen.dog_item_image_size))
            )
            Text(text = dog.replaceFirstChar(Char::uppercase), style = MaterialTheme.typography.h6)
        }
    }

}