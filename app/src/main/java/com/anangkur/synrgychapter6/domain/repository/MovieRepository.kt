package com.anangkur.synrgychapter6.domain.repository

import com.anangkur.synrgychapter6.domain.Movie

interface MovieRepository {
    suspend fun fetchMovies(): List<Movie>
}