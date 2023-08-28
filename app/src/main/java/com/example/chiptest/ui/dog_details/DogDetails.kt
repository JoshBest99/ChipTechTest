package com.example.chiptest.ui.dog_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.util.lerp
import com.example.chiptest.R
import com.example.chiptest.ui.ImageViaUrl
import kotlin.math.absoluteValue

@Composable
fun DogDetails(dogDetailsViewModel: DogDetailsViewModel) {
    val dogDetailsUiState by dogDetailsViewModel.dogDetailsUiState.collectAsState()

    DogDetails(uiState = dogDetailsUiState) {
        dogDetailsViewModel.getDogImages()
    }
}

@Composable
private fun DogDetails(uiState: DogDetailsUiState, refreshImages: () -> Unit) {
    Column(
        Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(visible = uiState.error != null) {
            Column(
                verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "${uiState.error}", style = MaterialTheme.typography.h4.copy(color = Color.Red), textAlign = TextAlign.Center)
                if (!uiState.isRefreshing) {
                    Button(onClick = { refreshImages() }) {
                        Text(text = "Try Again", style = MaterialTheme.typography.h5)
                    }
                }
            }
        }

        AnimatedVisibility(visible = uiState.isRefreshing) {
            CircularProgressIndicator(modifier = Modifier.size(dimensionResource(id = R.dimen.loading_icon_size)))
        }

        if (uiState.error == null && !uiState.isRefreshing) {
            ImagePager(dog = uiState.dog, images = uiState.images)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImagePager(dog: String, images: List<String>) {
    val pagerState = rememberPagerState(pageCount = {
        images.size
    })

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = dog.replaceFirstChar(Char::uppercase), style = MaterialTheme.typography.h5)

        HorizontalPager(
            state = pagerState
        ) { currentPage ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Card(
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.small_spacing))
                        .align(Alignment.Center)
                        .graphicsLayer {
                            val pageOffset = ((pagerState.currentPage - currentPage) + pagerState.currentPageOffsetFraction).absoluteValue

                            alpha = lerp(
                                start = 0.1f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        },
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.card_rounding)),
                    elevation = dimensionResource(id = R.dimen.medium_spacing)
                ) {
                    ImageViaUrl(url = images[currentPage], modifier = Modifier.size(dimensionResource(id = R.dimen.dog_image_size)))
                }
            }

        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.large_spacing)))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            repeat(images.size) { currentPage ->
                val color = if (pagerState.currentPage == currentPage) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.extra_small_spacing))
                        .clip(CircleShape)
                        .background(color)
                        .size(dimensionResource(id = R.dimen.dog_pager_indicator_size))

                )
            }
        }
    }

}