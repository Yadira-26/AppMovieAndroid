package com.example.appandroidmovie

object AppDestinations {
    const val HOME_ROUTE = "home"
    const val SEARCH_ROUTE = "search"
    const val FAVORITES_ROUTE = "favorites"
    const val MOVIE_DETAIL_ROUTE = "movie_detail"
    const val MOVIE_ID_ARG = "movieId"
    const val MOVIE_DETAIL_WITH_ARG_ROUTE = "$MOVIE_DETAIL_ROUTE/{$MOVIE_ID_ARG}"
}