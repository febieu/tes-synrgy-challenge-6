package com.anangkur.synrgychapter6.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.anangkur.synrgychapter6.domain.repository.ProfileRepository
import com.anangkur.synrgychapter6.helper.worker.TAG_OUTPUT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val workManager: WorkManager,
) : ViewModel() {

    internal val outputWorkerInfos = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _email = MutableLiveData<String?>()
    val email: LiveData<String?> = _email

    private val _username = MutableLiveData<String?>()
    val username: LiveData<String?> = _username

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _logout = MutableLiveData<Boolean>()
    val logout: LiveData<Boolean> = _logout

    fun loadProfile() {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.loadUsername()
                .combine(
                    profileRepository.loadEmail()
                ) { username, email ->
                    Pair(username, email)
                }
                .catch { throwable ->
                    withContext(Dispatchers.Main) {
                        _error.value = throwable.message
                        _loading.value = false
                    }
                }.collectLatest { (username, email) ->
                    withContext(Dispatchers.Main) {
                        _username.value = username
                        _email.value = email
                        _loading.value = false
                    }
                }
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.Main) {
            profileRepository.logout()
            _logout.value = true
        }
    }
}