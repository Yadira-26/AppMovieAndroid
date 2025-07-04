package com.example.appandroidmovie.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.appandroidmovie.R
import com.example.appandroidmovie.model.Movie
import com.example.appandroidmovie.network.MovieService
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movieId: Int, // El ID de la película que se pasará
    movieViewModel: MovieViewModel,
    navController: NavController // Para el botón de retroceso
) {
    // Observa los detalles de la película desde el ViewModel
    // Necesitarás una función en tu ViewModel para cargar detalles por ID
    // y un LiveData/StateFlow para observar el resultado.
    // Por ahora, asumiremos que el ViewModel puede buscar la película
    // de la lista actual o hacer una nueva llamada a la API.

    // Ejemplo: Si ya tienes la película en la lista de populares
    // Esto es una simplificación. Idealmente, tendrías una función en el ViewModel
    // para obtener detalles, que podría implicar una nueva llamada a la API.
    LaunchedEffect(movieId) {
        movieViewModel.fetchMovieById(movieId) // Necesitarás implementar esto
    }

    val movieDetail: Movie? by movieViewModel.selectedMovie.observeAsState()
    val isLoading: Boolean by movieViewModel.isLoadingDetail.observeAsState(false) // Nuevo estado para carga de detalles
    val errorMessage: String? by movieViewModel.errorMessageDetail.observeAsState() // Nuevo estado para error de detalles


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(movieDetail?.title ?: "Detalles de Película") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else if (errorMessage != null) {
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            } else if (movieDetail != null) {
                val movie = movieDetail!! // Sabemos que no es null aquí

                val posterUrl = MovieService.getPosterUrl(movie.posterPath)
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = posterUrl)
                            .apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                                placeholder(R.drawable.ic_launcher_background) // Asegúrate que existen
                                error(R.drawable.ic_launcher_foreground)
                            }).build()
                    ),
                    contentDescription = "Póster de ${movie.title}",
                    modifier = Modifier.size(200.dp, 300.dp), // Tamaño más grande para detalle
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Fecha de Lanzamiento: ${movie.releaseDate ?: "N/A"}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Puntuación: ${movie.voteAverage}/10 (${movie.voteCount} votos)")
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Resumen:", style = MaterialTheme.typography.titleMedium)
                Text(text = movie.overview)
                // Puedes añadir más detalles aquí (géneros, duración, etc.)
                // si tu modelo Movie y la API los proporcionan.
            } else {
                Text("No se encontraron detalles para esta película.")
            }
        }
    }
}