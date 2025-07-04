package com.example.appandroidmovie.ui


//import androidx.compose.ui.test.cancel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appandroidmovie.model.Movie
import com.example.appandroidmovie.network.MovieService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MovieViewModel : ViewModel() {
    // Estados y lógica para películas populares existentes

    var popularMovies by mutableStateOf<List<Movie>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Nuevos estados para la búsqueda
    var searchQuery by mutableStateOf("")
        private set // Solo el ViewModel puede modificar directamente

    var searchedMovies by mutableStateOf<List<Movie>>(emptyList())
    var isLoadingSearch by mutableStateOf(false)
    var errorMessageSearch by mutableStateOf<String?>(null)

    // Declare searchJob here
    private var searchJob: Job? = null

    // Para la película seleccionada en la pantalla de detalles
    private val _selectedMovie = MutableLiveData<Movie?>()
    val selectedMovie: LiveData<Movie?> = _selectedMovie

    private val _isLoadingDetail = MutableLiveData<Boolean>()
    val isLoadingDetail: LiveData<Boolean> = _isLoadingDetail

    private val _errorMessageDetail = MutableLiveData<String?>()
    val errorMessageDetail: LiveData<String?> = _errorMessageDetail

    // --- Inicio: Lógica para Favoritos ---
    private val _favoriteMovies = mutableStateListOf<Movie>() // Usamos mutableStateListOf para que Compose observe cambios en la lista
    val favoriteMovies: List<Movie> = _favoriteMovies // Exponemos como una lista inmutable para el exterior

    // LiveData o StateFlow para saber si la película actual es favorita
    private val _isCurrentMovieFavorite = MutableLiveData<Boolean>()
    val isCurrentMovieFavorite: LiveData<Boolean> = _isCurrentMovieFavorite

    init {
        fetchPopularMovies()
    }

    fun toggleFavorite(movie: Movie) {
        val isCurrentlyFavorite = _favoriteMovies.any { it.id == movie.id }
        if (isCurrentlyFavorite) {
            _favoriteMovies.removeIf { it.id == movie.id }
        } else {
            _favoriteMovies.add(movie)
        }
        // Actualiza el estado de la película actual si es la que se está mostrando
        if (movie.id == _selectedMovie.value?.id) {
            _isCurrentMovieFavorite.value = !isCurrentlyFavorite
        }
    }

    // Función para verificar si una película es favorita, útil al cargar detalles
    private fun checkIfFavorite(movieId: Int) {
        _isCurrentMovieFavorite.value = _favoriteMovies.any { it.id == movieId }
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
    // Nueva función para actualizar el query de búsqueda
    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
        searchJob?.cancel() // Cancela la búsqueda anterior si el usuario sigue escribiendo
        if (newQuery.length > 2) { // O la longitud que consideres para empezar a buscar
            searchJob = viewModelScope.launch {
                delay(500) // Debounce: espera 500ms después de que el usuario deja de escribir
                executeSearch(newQuery)
            }
        } else {
            searchedMovies = emptyList() // Limpia los resultados si el query es muy corto
            errorMessageSearch = null
        }
    }

    private fun executeSearch(query: String) {
        isLoadingSearch = true
        errorMessageSearch = null
        viewModelScope.launch {
            try {
                val response = MovieService.searchMovies(query)
                searchedMovies = response.results
                if (searchedMovies.isEmpty()) {
                    errorMessageSearch = "No se encontraron películas para '$query'."
                }
            } catch (e: Exception) {
                errorMessageSearch = "Error al buscar películas: ${e.message}"
            } finally {
                isLoadingSearch = false
            }
        }
    }

    // Función para obtener detalles de una película (simplificado)
    // Idealmente, harías una llamada a un endpoint de API como /movie/{movie_id}
    fun fetchMovieById(movieId: Int) {
        viewModelScope.launch {
            _isLoadingDetail.value = true
            _errorMessageDetail.value = null
            try {
                // Opción 1: Si tienes un endpoint para detalles de película
                val movieDetail = MovieService.getMovieDetails(movieId) // Necesitarías crear esta función en MovieService
                _selectedMovie.value = movieDetail

                // Opción 2: Si solo buscas en la lista de populares ya cargada (menos ideal para detalles completos)
                // val movie = popularMovies.find { it.id == movieId }
                // _selectedMovie.value = movie
                // if (movie == null) {
                //     _errorMessageDetail.value = "Película no encontrada en la lista actual."
                // }

            } catch (e: Exception) {
                _errorMessageDetail.value = "Error al cargar detalles: ${e.message}"
            } finally {
                _isLoadingDetail.value = false
            }
        }
    }
}
