package com.example.appandroidmovie.ui


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appandroidmovie.model.Movie
import com.example.appandroidmovie.network.MovieService
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    var popularMovies by mutableStateOf<List<Movie>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchPopularMovies()
    }

    fun fetchPopularMovies() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = MovieService.getPopularMovies(language = "es-ES")
                popularMovies = response?.results ?: emptyList()
            } catch (e: Exception) {
                errorMessage = "Error al cargar las películas: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}