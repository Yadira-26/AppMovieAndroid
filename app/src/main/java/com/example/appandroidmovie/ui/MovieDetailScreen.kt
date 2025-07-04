package com.example.appandroidmovie.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.appandroidmovie.R
import com.example.appandroidmovie.model.Movie
import com.example.appandroidmovie.network.MovieService


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
    val isFavorite: Boolean by movieViewModel.isCurrentMovieFavorite.observeAsState(false)


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
                },
                actions = {
                    movieDetail?.let { movie ->
                        IconButton(onClick = {movieViewModel.toggleFavorite(movie)}) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Añadir a Favoritos",
                                tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        // Crea un estado de scroll
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Aplica el padding del Scaffold
                .verticalScroll(scrollState) // ¡Aquí está la magia!
                .padding(16.dp), // Padding adicional para el contenido interno
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                val error = errorMessage
                if (error != null) {
                    Text(text = error, color = MaterialTheme.colorScheme.error)
                } else if (movieDetail != null) {
                    val movie = movieDetail!!

                    val posterUrl = MovieService.getPosterUrl(movie.posterPath)
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = posterUrl)
                                .apply {
                                    crossfade(true)
                                    placeholder(R.drawable.ic_launcher_background)
                                    error(R.drawable.ic_launcher_foreground)
                                }.build()
                        ),
                        contentDescription = "Póster de ${movie.title}",
                        modifier = Modifier.size(200.dp, 300.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Fecha de Lanzamiento: ${movie.releaseDate ?: "N/A"}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Puntuación: ${movie.voteAverage}/10 (${movie.voteCount} votos)")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Resumen:", style = MaterialTheme.typography.titleMedium)
                    // Asegúrate de que movie.overview pueda ser largo
                    Text(text = movie.overview)

                    // Puedes añadir mucho más contenido aquí para probar el scroll
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Text("Más información ficticia:")
//                    repeat(20) { // Añade contenido extra para asegurar que el scroll sea necesario
//                        Text("Línea de detalle adicional número ${it + 1} para probar el scroll y ver cómo se comporta la pantalla con mucho contenido.")
//                        Spacer(modifier = Modifier.height(4.dp))
//                    }
                } else {
                    Text("No se encontraron detalles para esta película.")
                }
            }
        }

    }
}

