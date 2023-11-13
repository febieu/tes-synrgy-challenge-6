package com.anangkur.synrgychapter6.domain.repository

import com.anangkur.synrgychapter6.domain.Movie

interface HomeRepository {
    suspend fun fetchMovies(): List<Movie>
}