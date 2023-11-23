package com.anangkur.synrgychapter6.data.local

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.anangkur.synrgychapter6.domain.repository.LoginRepository
import com.anangkur.synrgychapter6.domain.repository.ProfileRepository
import com.anangkur.synrgychapter6.domain.repository.RegisterRepository
import com.anangkur.synrgychapter6.helper.isEmailValid
import com.anangkur.synrgychapter6.helper.isPasswordValid
import com.anangkur.synrgychapter6.data.local.DataStoreManager
import com.anangkur.synrgychapter6.domain.repository.AccountRepository
import com.anangkur.synrgychapter6.domain.repository.AuthRepository
import com.anangkur.synrgychapter6.domain.repository.ImageRepository
import com.anangkur.synrgychapter6.helper.worker.BlurWorker
import com.anangkur.synrgychapter6.helper.worker.IMAGE_MANIPULATION_WORK_NAME
import com.anangkur.synrgychapter6.helper.worker.KEY_IMAGE_URI
import com.anangkur.synrgychapter6.helper.worker.TAG_OUTPUT
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class LocalRepository(
    private val dataStoreManager: DataStoreManager,
    //CA
    private val workManager: WorkManager,
    ): AccountRepository, AuthRepository, ImageRepository {
    //) : LoginRepository, RegisterRepository, ProfileRepository {

    override suspend fun validateInput(username: String, password: String): Boolean {
        delay(1000)
        return username.isNotEmpty()
                && username.isNotBlank()
                && password.isNotEmpty()
                && password.isNotBlank()
    }

    override suspend fun authenticate(username: String, password: String): String {
        delay(1000)
        return if (username == "anangkur" && password == "123456") {
            "token"
        } else {
            throw UnsupportedOperationException("username dan password salah!")
        }
    }

    override suspend fun saveToken(token: String) {
        dataStoreManager.saveToken(token)
    }

    override suspend fun isLoggedIn(): Flow<Boolean?> {
        return combine(
            dataStoreManager.loadToken(),
            dataStoreManager.loadUsername(),
            dataStoreManager.loadEmail(),
        ) { token, username, email ->
            (!token.isNullOrEmpty() && token.isNotBlank()) || (!username.isNullOrEmpty() && username.isNotBlank() && !email.isNullOrEmpty() && email.isNotBlank())
        }
    }

    override suspend fun validateInput(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return username.isNotEmpty()
                && username.isNotBlank()
                && email.isNotEmpty()
                && email.isNotBlank()
                && email.isEmailValid()
                && password.isNotEmpty()
                && password.isNotBlank()
                && password.isPasswordValid()
                && password == confirmPassword
    }

    override suspend fun register(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        delay(1000)
        dataStoreManager.saveUsername(username)
        dataStoreManager.saveEmail(email)
    }

    override suspend fun loadUsername(): Flow<String?> {
        return dataStoreManager.loadUsername()
    }

    override suspend fun loadEmail(): Flow<String?> {
        return dataStoreManager.loadEmail()
    }

    override suspend fun logout() {
        delay(1000)
        dataStoreManager.deleteEmail()
        dataStoreManager.deleteUsername()
        dataStoreManager.deleteToken()
    }

    override suspend fun loadProfilePhoto(): Flow<String?>{
        return dataStoreManager.loadProfilePhoto()
    }

    override suspend fun saveProfilePhoto(profilePhoto: String) {
        dataStoreManager.saveProfilePhoto(profilePhoto)
    }

    /*
    loadProfilePhoto
    saveProfilePhoto
    bakal ditambah di account repo
        00:36:27
    applyBlur / applyMonochrome
    setinputdataforuri
    getworkmanagerlivedata
        tambah di imagerepository
     */

    override fun applyBlur(imageUri: Uri?) {
        workManager.beginUniqueWork(
            IMAGE_MANIPULATION_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<BlurWorker>()
                .setInputData(setInputDataForUri(imageUri))
                .addTag(TAG_OUTPUT)
                .build()
        ).enqueue()
    }
    override fun setInputDataForUri(imageUri: Uri?): Data {
        return Data.Builder().apply {
            putString(KEY_IMAGE_URI, imageUri?.toString())
        }.build()
    }

    override fun getWorkManagerLiveData(): LiveData<List<WorkInfo>> {
        return workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)
    }

}