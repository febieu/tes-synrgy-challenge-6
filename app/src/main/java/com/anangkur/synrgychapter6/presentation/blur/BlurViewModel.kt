/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anangkur.synrgychapter6.presentation.blur

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.anangkur.synrgychapter6.domain.repository.AccountRepository
import com.anangkur.synrgychapter6.domain.repository.ImageRepository
import com.anangkur.synrgychapter6.helper.toUriOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class BlurViewModel(
    //00:38:20
    //pake image repo dn account repo utk CA
    private val imageRepository: ImageRepository,
    private val accountRepository: AccountRepository,
//    private val workManager: WorkManager,
//    private val application: Application,
) : ViewModel() {
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _profilePhoto = MutableLiveData<String?>()
    val profilePhoto: LiveData<String?> = _profilePhoto

    private var imageUri: Uri? = null
    private var outputUri: Uri? = null

    internal fun setOutputUri(outputImageUri: String?) {
        outputUri = outputImageUri.toUriOrNull()
    }

    internal fun setImageUri(imageUri: String?) {
        this.imageUri = imageUri.toUriOrNull()
    }

    fun saveProfilePhoto(profilePhoto: String) {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.saveProfilePhoto(profilePhoto)
        }
    }

    fun applyBlur() {
        imageRepository.applyBlur(imageUri)
    }

    fun getOutputWorkerInfo(): LiveData<List<WorkInfo>> {
        return imageRepository.getWorkManagerLiveData()
    }

    fun loadProfilePhoto() {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.loadProfilePhoto()
                .catch { throwable ->
                    withContext(Dispatchers.Main) {
                        _error.value = throwable.message
                    }
                }
                .collectLatest { profilePhoto ->
                    withContext(Dispatchers.Main) {
                        _profilePhoto.value = profilePhoto
                    }
                }
        }
    }

}
