package com.anangkur.synrgychapter6.presentation.auth.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anangkur.synrgychapter6.databinding.ActivityRegisterBinding
import com.anangkur.synrgychapter6.presentation.home.HomeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, RegisterActivity::class.java))
        }
    }

    private var binding: ActivityRegisterBinding? = null
    private val viewModel by viewModel<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        observeLiveData()

        binding?.buttonRegister?.setOnClickListener {
            viewModel.register(
                username = binding?.etUsername?.text?.toString().orEmpty(),
                password = binding?.etPassword?.text?.toString().orEmpty(),
                confirmPassword = binding?.etConfirmPassword?.text?.toString().orEmpty(),
                email = binding?.etEmail?.text?.toString().orEmpty(),
            )
        }
    }

    private fun observeLiveData() {
        viewModel.loading.observe(this, ::handleLoading)
        viewModel.error.observe(this, ::handleError)
        viewModel.register.observe(this) { handleRegister() }
    }

    private fun handleLoading(isLoading: Boolean) {
        binding?.flipperButton?.displayedChild = if (isLoading) 1 else 0
    }

    private fun handleError(error: String?) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    private fun handleRegister() {
        Toast.makeText(this, "Register succeed!", Toast.LENGTH_SHORT).show()
        HomeActivity.startActivity(this)
        finish()
    }
}