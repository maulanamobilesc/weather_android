package com.maulana.weathermobile.ui.page.managelocation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.maulana.warehouse.core.component.LoadingComponent
import com.maulana.warehouse.core.component.PageErrorMessageHandler
import com.maulana.warehouse.domain.UIState
import com.maulana.weathermobile.core.component.SwipeToDeleteItem
import com.maulana.weathermobile.core.component.WeatherIcon
import com.maulana.weathermobile.core.component.WeatherIconSize
import com.maulana.weathermobile.domain.LocationIntent
import com.maulana.weathermobile.domain.model.LocationLocal
import com.maulana.weathermobile.domain.model.WeatherLocal
import com.maulana.weathermobile.util.AppColor
import com.maulana.weathermobile.util.GlobalDimension

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageLocationScreen(
    navController: NavHostController,
    snackBarState: SnackbarHostState,
    viewModel: ManageLocationViewModel = hiltViewModel()
) {
    viewModel.apply {
        val savedWeatherUiState by savedWeatherUiState.collectAsState()
        val searchResultUiState by searchResultUiState.collectAsState()

        LaunchedEffect(Unit) {
            processIntent(LocationIntent.LoadSavedWeather)
        }

        if (savedWeatherUiState is UIState.Error) {
            PageErrorMessageHandler((savedWeatherUiState as UIState.Error).message, snackBarState)
        }

        Card(
            shape = RoundedCornerShape(30.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(GlobalDimension.sectionPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF62B8F6),
                                Color(0xFF2C79C1)
                            )
                        ), alpha = 1.0f
                    )
            ) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White // Set the color of the back arrow to white
                        )
                    }
                    Text(
                        text = "Manage location",
                        color = Color.White
                    )
                }

                val colors1 = SearchBarDefaults.colors()
                var searchBarActive by rememberSaveable { mutableStateOf(false) }

                SearchBar(
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = query.value,
                            onQueryChange = { query.value = it },
                            onSearch = {
                                processIntent(LocationIntent.SearchLocation)
                            },
                            expanded = searchBarActive,
                            onExpandedChange = { searchBarActive = it },
                            enabled = true,
                            placeholder = {
                                Text(text = "Search Your City", color = Color.Gray)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Icon",
                                    tint = Color.Black
                                )
                            },
                            trailingIcon = null,
                            colors = colors1.inputFieldColors,
                            interactionSource = null,
                        )
                    },
                    expanded = searchBarActive,
                    onExpandedChange = { searchBarActive = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(GlobalDimension.sectionPadding),
                    shape = SearchBarDefaults.inputFieldShape,
                    colors = colors1,
                    tonalElevation = SearchBarDefaults.TonalElevation,
                    shadowElevation = SearchBarDefaults.ShadowElevation,
                    windowInsets = SearchBarDefaults.windowInsets,
                    {
                        if (query.value.isNotEmpty() && searchResultUiState is UIState.Success) {
                            (searchResultUiState as UIState.Success<List<LocationLocal>>).data.forEach { item ->
                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(GlobalDimension.sectionPadding)
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(item.name, color = AppColor.secondaryTextColor)
                                            Text(
                                                "${item.state}, ${item.country}",
                                                color = AppColor.secondaryTextColor
                                            )
                                        }
                                        IconButton(onClick = {
                                            processIntent(
                                                LocationIntent.GetWeatherFromSelectedCity(
                                                    item
                                                )
                                            )
                                            searchBarActive = false
                                        }) {
                                            Icon(imageVector = Icons.Default.Add, "Add Icon")
                                        }
                                    }
                                    HorizontalDivider(
                                        thickness = 1.dp,
                                        color = Color.Black,
                                        modifier = Modifier.padding(horizontal = GlobalDimension.smallPadding)
                                    )
                                }
                            }
                        }
                    })
                if (savedWeatherUiState is UIState.Success) {
                    LazyColumn(
                        Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(GlobalDimension.sectionPadding),
                        verticalArrangement = Arrangement.spacedBy(GlobalDimension.sectionPadding)
                    ) {
                        items(items = (savedWeatherUiState as UIState.Success<List<WeatherLocal>>).data) { item ->
                            SwipeToDeleteItem(120.dp, onClickDelete = {
                                processIntent(LocationIntent.DeleteLocation(item.locationId))
                            }) {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .background(
                                            color = Color.White,
                                            shape = RoundedCornerShape(16.dp)
                                        ),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.padding(start = GlobalDimension.sectionPadding)) {
                                        Text(
                                            item.locationName,
                                            fontSize = GlobalDimension.defaultFontSize,
                                            color = AppColor.secondaryTextColor,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            "${item.minTemperature.toInt()}°c /${item.maxTemperature.toInt()}°c",
                                            fontSize = GlobalDimension.smallFontSize,
                                            color = AppColor.secondaryTextColor,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Column(
                                        modifier = Modifier.padding(GlobalDimension.sectionPadding),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        WeatherIcon(item.weatherCode, WeatherIconSize.SMALL)
                                        Text(
                                            item.currentWeather,
                                            fontSize = GlobalDimension.smallFontSize,
                                            color = AppColor.secondaryTextColor,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    LoadingComponent()
                }
            }
        }
    }
}