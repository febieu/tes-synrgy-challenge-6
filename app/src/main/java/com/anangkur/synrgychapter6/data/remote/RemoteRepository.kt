package com.anangkur.synrgychapter6.data.remote

import com.anangkur.synrgychapter6.data.remote.response.toMovie
import com.anangkur.synrgychapter6.data.remote.service.TMDBService
import com.anangkur.synrgychapter6.domain.Movie
import com.anangkur.synrgychapter6.domain.repository.HomeRepository
import com.anangkur.synrgychapter6.domain.repository.MovieRepository
import kotlinx.coroutines.delay

class RemoteRepository(
    private val tmdbService: TMDBService,
    ) : MovieRepository {
//) : HomeRepository {
    override suspend fun fetchMovies(): List<Movie> {
        return tmdbService.fetchMovies().results?.map { result -> result.toMovie() }.orEmpty()
    }
}