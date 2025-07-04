package com.example.appandroidmovie.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.appandroidmovie.R // Importa R si usas drawables como placeholder/error
import com.example.appandroidmovie.model.Movie
import com.example.appandroidmovie.network.MovieService
import com.example.appandroidmovie.ui.theme.AppAndroidMovieTheme // Si usas tu tema en la Preview

// El código de MovieItem que pegaste
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
            // Log.d("MovieItem", "Película: ${movie.title}, Poster Path: ${movie.posterPath}") // Puedes mantener tus logs
            // Log.d("MovieItem", "Cargando imagen desde URL: $posterUrl")

            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = posterUrl)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                            // Asegúrate que estos drawables existen o comenta estas líneas
                            placeholder(R.drawable.ic_launcher_background)
                            error(R.drawable.ic_launcher_foreground)
                        }).build()
                ),
                contentDescription = "Póster de ${movie.title}",
                modifier = Modifier
                    .size(100.dp, 150.dp)
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

// También puedes mover la Preview de MovieItem aquí
@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    AppAndroidMovieTheme { // Asegúrate que tu tema sea accesible o usa un tema genérico
        MovieItem(
            movie = Movie(
                id = 1,
                title = "Título de Película de Ejemplo Muy Largo Que Podría Desbordarse",
                posterPath = "/ejemplo.jpg",
                overview = "Esta es una breve descripción de la película de ejemplo...",
                releaseDate = "2023-01-01",
                voteAverage = 7.5
            )
        )
    }
}