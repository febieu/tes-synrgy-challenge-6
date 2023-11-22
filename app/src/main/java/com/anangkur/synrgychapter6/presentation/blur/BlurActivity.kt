package com.anangkur.synrgychapter6.presentation.blur

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.WorkInfo
import com.anangkur.synrgychapter6.databinding.ActivityBlurBinding
import com.anangkur.synrgychapter6.helper.worker.KEY_IMAGE_URI
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.ImagePickerMode
import com.esafirm.imagepicker.features.registerImagePicker
import com.esafirm.imagepicker.model.Image
import org.koin.androidx.viewmodel.ext.android.viewModel

class BlurActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, BlurActivity::class.java))
        }
    }

    private val binding by lazy { ActivityBlurBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<BlurViewModel>()
    private val imagePickerLauncher = registerImagePicker(callback = ::handleImageResult)
    private val imagePickerConfig by lazy { ImagePickerConfig(mode = ImagePickerMode.SINGLE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observeLiveData()

        binding.buttonGo.setOnClickListener { viewModel.applyBlur() }
        binding.ivBlur.setOnClickListener { imagePickerLauncher.launch(config = imagePickerConfig) }
    }

    private fun observeLiveData() {
        viewModel.outputWorkerInfos.observe(this, ::handleWorkerInfos)
    }

    private fun handleWorkerInfos(workerInfos: List<WorkInfo>) {
        if (workerInfos.isEmpty()) {
            return
        }

        val workerInfo = workerInfos.last()
        if (workerInfo.state.isFinished) {
            val outputImageUrl = workerInfo.outputData.getString(KEY_IMAGE_URI)
            if (!outputImageUrl.isNullOrEmpty()) {
                viewModel.setOutputUri(outputImageUrl)
                binding.ivBlur.setImageURI(Uri.parse(outputImageUrl))
            }
        } else {
            // todo handle in progress
        }
    }

    private fun handleImageResult(images: List<Image>) {
        images.firstOrNull()?.let { image ->
            binding.ivBlur.setImageURI(image.uri)
            viewModel.setImageUri(image.uri.toString())
        }
    }
}