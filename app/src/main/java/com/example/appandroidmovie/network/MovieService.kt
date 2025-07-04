package com.example.appandroidmovie.network

import com.example.appandroidmovie.model.MovieResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


object MovieService {
    // RECUERDA: Reemplaza "TU_CLAVE_DE_API" con tu clave real
    private const val API_KEY = "6c49e65076398ff0aeb2b935f7e947b9"
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500" // Para construir la URL completa del póster

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true // Ignora campos desconocidos en la respuesta JSON
                prettyPrint = true
                isLenient = true
            })
        }
    }

    suspend fun getPopularMovies(language: String = "es-ES", page: Int = 1): MovieResponse? {
        return try {
            client.get("${BASE_URL}movie/popular") {
                parameter("api_key", API_KEY)
                parameter("language", language)
                parameter("page", page)
            }.body()
        } catch (e: Exception) {
            // Maneja errores de red o deserialización aquí
            e.printStackTrace()
            null
        }
    }

    fun getPosterUrl(posterPath: String?): String? {
        return posterPath?.let { "$IMAGE_BASE_URL$it" }
    }
}