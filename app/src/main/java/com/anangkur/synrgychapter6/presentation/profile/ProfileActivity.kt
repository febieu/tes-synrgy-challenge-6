package com.anangkur.synrgychapter6.presentation.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.WorkInfo
import com.anangkur.synrgychapter6.databinding.ActivityProfileBinding
import com.anangkur.synrgychapter6.helper.worker.KEY_IMAGE_URI
import com.anangkur.synrgychapter6.presentation.auth.login.LoginActivity
import com.anangkur.synrgychapter6.presentation.blur.BlurActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ProfileActivity::class.java))
        }
    }

    private var binding: ActivityProfileBinding? = null
    private val viewModel by viewModel<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        observeLiveData()

        viewModel.loadProfile()

        binding?.buttonLogout?.setOnClickListener {
            viewModel.logout()
        }

        binding?.ivProfile?.setOnClickListener {
            BlurActivity.startActivity(this)
        }
    }

    private fun observeLiveData() {
        viewModel.username.observe(this, ::handleUsername)
        viewModel.email.observe(this, ::handleEmail)
        viewModel.loading.observe(this, ::handleLoading)
        viewModel.error.observe(this, ::handleError)
        viewModel.logout.observe(this, ::handleLogout)
        viewModel.outputWorkerInfos.observe(this, ::handleWorkerInfos)
    }

    private fun handleUsername(username: String?) {
        binding?.etUsername?.setText(username)
    }

    private fun handleEmail(email: String?) {
        binding?.etEmail?.setText(email)
    }

    private fun handleLoading(isLoading: Boolean) {
        binding?.flipperButton?.displayedChild = if (isLoading) 1 else 0
    }

    private fun handleError(error: String?) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    private fun handleLogout(isLogout: Boolean) {
        if (isLogout) {
            LoginActivity.startActivity(this)
        }
    }

    private fun handleWorkerInfos(workerInfos: List<WorkInfo>) {
        if (workerInfos.isEmpty()) {
            return
        }

        val workerInfo = workerInfos.last()
        if (workerInfo.state.isFinished) {
            val outputImageUrl = workerInfo.outputData.getString(KEY_IMAGE_URI)
            if (!outputImageUrl.isNullOrEmpty()) {
                binding?.ivProfile?.setImageURI(Uri.parse(outputImageUrl))
            }
        } else {
            // todo handle in progress
        }
    }
}