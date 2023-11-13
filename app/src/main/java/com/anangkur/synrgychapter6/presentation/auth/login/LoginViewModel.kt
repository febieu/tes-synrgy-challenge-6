package com.anangkur.synrgychapter6.presentation.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anangkur.synrgychapter6.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val loginRepository: LoginRepository,
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _authentication = MutableLiveData<String>()
    val authentication: LiveData<String> = _authentication

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun authenticate(username: String, password: String) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            if (loginRepository.validateInput(username, password)) {
                try {
                    withContext(Dispatchers.Main) {
                        _authentication.value = loginRepository.authenticate(username, password)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _error.value = e.message
                    }
                } finally {
                    withContext(Dispatchers.Main) {
                        _loading.value = false
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    _error.value = "username atau password tidak valid!"
                    _loading.value = false
                }
            }
        }
    }

    fun saveToken(token: String) {
        viewModelScope.launch(Dispatchers.Main) {
            loginRepository.saveToken(token)
        }
    }

    fun checkLogin() {
        viewModelScope.launch(Dispatchers.IO) {
            loginRepository.isLoggedIn().collectLatest { isLoggedIn ->
                if (isLoggedIn == true) {
                    withContext(Dispatchers.Main) {
                        _authentication.value = "token"
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _authentication.value = ""
                    }
                }
            }
        }
    }
}