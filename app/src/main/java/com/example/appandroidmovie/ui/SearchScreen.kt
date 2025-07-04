package com.example.appandroidmovie.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appandroidmovie.ui.theme.AppAndroidMovieTheme

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Pantalla de Búsqueda (Próximamente)")
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    AppAndroidMovieTheme {
        SearchScreen()
    }
}