package com.example.appandroidmovie.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appandroidmovie.ui.theme.AppAndroidMovieTheme

@OptIn(ExperimentalMaterial3Api::class) // Para TextField
@Composable
fun SearchScreen(
    movieViewModel: MovieViewModel, // Recibe el ViewModel
    modifier: Modifier = Modifier
) {
    val searchQuery = movieViewModel.searchQuery
    val searchedMovies = movieViewModel.searchedMovies
    val isLoading = movieViewModel.isLoadingSearch
    val errorMessage = movieViewModel.errorMessageSearch

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField( // O TextField normal
            value = searchQuery,
            onValueChange = { movieViewModel.onSearchQueryChange(it) },
            label = { Text("Buscar películas...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }
        } else if (searchQuery.isNotBlank() && searchedMovies.isEmpty() && !isLoading) {
            // Este caso se maneja con el errorMessageSearch, pero podrías tener un mensaje
            // específico si lo prefieres, por ejemplo, si el query se ha limpiado
            // y ya no hay error pero tampoco resultados.
            // Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            // Text("Ingresa un término para buscar películas.")
            // }
        } else if (searchedMovies.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(searchedMovies) { movie ->
                    MovieItem(movie = movie) // Reutilizamos el MovieItem
                }
            }
        } else if (searchQuery.isBlank()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ingresa un término para buscar películas.")
            }
        }
        // No mostramos nada si el query está vacío y no hay error/carga (estado inicial)
        // o si el query es corto y se limpiaron los resultados.
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    AppAndroidMovieTheme {
        SearchScreen(
            movieViewModel = TODO(),
            modifier = TODO()
        )
        // Para una preview funcional, necesitarías instanciar un ViewModel,
        // lo cual puede ser complejo para previews simples.
        // Aquí mostramos solo la estructura básica de la UI sin datos dinámicos.
        Column(Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = "Ejemplo de búsqueda",
                onValueChange = {},
                label = { Text("Buscar películas...") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Los resultados de la búsqueda aparecerán aquí.")
            }
        }
    }
}