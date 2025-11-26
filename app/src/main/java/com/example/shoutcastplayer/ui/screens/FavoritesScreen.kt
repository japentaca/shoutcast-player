package com.example.shoutcastplayer.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.shoutcastplayer.data.model.StationDto
import com.example.shoutcastplayer.ui.components.StationItem
import com.example.shoutcastplayer.ui.navigation.Screen
import com.example.shoutcastplayer.ui.viewmodel.RadioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: RadioViewModel,
    navController: NavController
) {
    val favorites by viewModel.favorites.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorites") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(favorites) { favorite ->
                // Convert FavoriteStation to StationDto for reuse (or create a mapper)
                // For MVP simplicity, we construct Dto here. Ideally, use a Mapper.
                val stationDto = StationDto(
                    stationUuid = favorite.stationUuid,
                    name = favorite.name,
                    url = favorite.url,
                    urlResolved = favorite.url,
                    homepage = favorite.homepage,
                    favicon = favorite.favicon,
                    tags = favorite.tags,
                    country = favorite.country,
                    state = "",
                    language = "",
                    votes = 0,
                    codec = "",
                    bitrate = favorite.bitrate
                )
                
                StationItem(
                    station = stationDto,
                    onClick = {
                        viewModel.playStation(stationDto)
                        navController.navigate(Screen.Player.route)
                    }
                )
            }
        }
    }
}
