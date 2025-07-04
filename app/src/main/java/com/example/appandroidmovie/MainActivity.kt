package com.example.appandroidmovie


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels // Importa viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Importa items para LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter // Importa rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.appandroidmovie.model.Movie // Importa tu modelo Movie
import com.example.appandroidmovie.network.MovieService
import com.example.appandroidmovie.ui.MovieViewModel // Importa tu ViewModel
import com.example.appandroidmovie.ui.theme.AppAndroidMovieTheme

class MainActivity : ComponentActivity() {
    // Inyecta el ViewModel
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppAndroidMovieTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PopularMoviesScreen(
                        movieViewModel = movieViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PopularMoviesScreen(movieViewModel: MovieViewModel, modifier: Modifier = Modifier) {
    val movies = movieViewModel.popularMovies
    val isLoading = movieViewModel.isLoading
    val errorMessage = movieViewModel.errorMessage

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else if (errorMessage != null) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        } else if (movies.isEmpty()) {
            Text("No se encontraron películas populares.")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(movies) { movie ->
                    MovieItem(movie = movie)
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val posterUrl = MovieService.getPosterUrl(movie.posterPath)
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = posterUrl)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true) // Efecto de fundido
                            placeholder(R.drawable.ic_launcher_background) // Imagen de placeholder (crea este drawable o usa uno existente)
                            error(R.drawable.ic_launcher_foreground) // Imagen de error (crea este drawable o usa uno existente)
                        }).build()
                ),
                contentDescription = "Póster de ${movie.title}",
                modifier = Modifier
                    .size(100.dp, 150.dp) // Tamaño del póster
                    .padding(end = 8.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Lanzamiento: ${movie.releaseDate ?: "N/A"}",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Puntuación: ${movie.voteAverage}/10",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// Preview actualizada (o puedes crear una nueva para PopularMoviesScreen)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppAndroidMovieTheme {
        // Para el preview, podrías pasar un ViewModel con datos de ejemplo
        // o un Composable más simple si PopularMoviesScreen es complejo de previsualizar directamente.
        // Aquí mantendremos Greeting para simplicidad del ejemplo original.
        Text("Vista previa de la app de películas")
    }
}

// Puedes crear una preview específica para MovieItem si lo deseas:
@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    AppAndroidMovieTheme {
        MovieItem(
            movie = Movie(
                id = 1,
                title = "Título de Película de Ejemplo Muy Largo Que Podría Desbordarse",
                posterPath = "/ejemplo.jpg",
                overview = "Esta es una breve descripción de la película de ejemplo. Es interesante y captura la esencia de la trama sin revelar demasiado. Debería ser lo suficientemente larga como para probar el truncamiento.",
                releaseDate = "2023-01-01",
                voteAverage = 7.5
            )
        )
    }
}