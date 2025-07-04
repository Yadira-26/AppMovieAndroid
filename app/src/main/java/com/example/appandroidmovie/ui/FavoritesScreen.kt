package com.example.appandroidmovie.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    movieViewModel: MovieViewModel, // Asumiendo que se lo pasas
    navController: NavController    // Para navegar a detalles si es necesario
) {
    val favoriteMovies = movieViewModel.favoriteMovies // Obtén la lista de favoritos

    if (favoriteMovies.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No tienes películas favoritas.")
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(favoriteMovies) { movie ->
                MovieItem( // Reutiliza tu MovieItem
                    movie = movie,
                    onMovieClick = { movieId ->
                        navController.navigate("${AppDestinations.MOVIE_DETAIL_ROUTE}/$movieId")
                    }
                )
                // Opcional: Añadir un botón para quitar de favoritos directamente desde esta pantalla
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview() {
    FavoritesScreen(
        modifier = Modifier,
        movieViewModel = MovieViewModel(),
        navController = NavController(LocalContext.current)
    )
}

