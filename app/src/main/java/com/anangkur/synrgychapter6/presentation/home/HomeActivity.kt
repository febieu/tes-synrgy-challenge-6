package com.anangkur.synrgychapter6.presentation.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.anangkur.synrgychapter6.Application
import com.anangkur.synrgychapter6.data.remote.service.TMDBService
import com.anangkur.synrgychapter6.databinding.ActivityHomeBinding
import com.anangkur.synrgychapter6.di.ViewModelFactory
import com.anangkur.synrgychapter6.domain.Movie
import com.anangkur.synrgychapter6.presentation.adapter.MovieAdapter
import com.anangkur.synrgychapter6.presentation.profile.ProfileActivity

class HomeActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    }

    private var binding: ActivityHomeBinding? = null
    private var movieAdapter: MovieAdapter? = null

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance((application as Application).provider)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        observeLiveData()
        setupMovieAdapter()

        binding?.buttonProfile?.setOnClickListener {
            ProfileActivity.startActivity(this)
        }

        viewModel.fetchMovies()
    }

    private fun setupMovieAdapter() {
        movieAdapter = MovieAdapter()
        binding?.recyclerMovie?.adapter = movieAdapter
        binding?.recyclerMovie?.layoutManager = LinearLayoutManager(this)
    }

    private fun observeLiveData() {
        viewModel.loading.observe(this, ::handleLoading)
        viewModel.error.observe(this, ::handleError)
        viewModel.movies.observe(this, ::handleMovies)
    }

    private fun handleLoading(isLoading: Boolean) {
        binding?.progress?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun handleError(error: String?) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    private fun handleMovies(movies: List<Movie>) {
        movieAdapter?.submitList(movies)
    }
}