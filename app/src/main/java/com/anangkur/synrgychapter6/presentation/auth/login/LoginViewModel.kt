package com.anangkur.synrgychapter6.presentation.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anangkur.synrgychapter6.presentation.auth.login.usecase.AuthenticateUseCase
import com.anangkur.synrgychapter6.presentation.auth.login.usecase.CheckLoginUseCase
import com.anangkur.synrgychapter6.presentation.auth.login.usecase.SaveTokenUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val authenticateUseCase: AuthenticateUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val checkLoginUseCase: CheckLoginUseCase,
    //01:04:00 -> usecase utk login
    //private val authRepository: AuthRepository,
    //private val loginRepository: LoginRepository,
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _authentication = MutableLiveData<String>()
    val authentication: LiveData<String> = _authentication

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun authenticate(
        username: String,
        password: String
    ) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try{
                withContext(Dispatchers.Main){
                    _authentication.value = authenticateUseCase.invoke(username, password)
                }
            } catch(throwable: Throwable){
                withContext((Dispatchers.Main)){
                    _error.value = throwable.message
                }
            } finally{
                withContext(Dispatchers.Main){
                    _loading.value = false
                }
            }

//            if (authRepository.validateInput(username, password)) {
//                try {
//                    withContext(Dispatchers.Main) {
//                        _authentication.value = authRepository.authenticate(username, password)
//                    }
//                } catch (e: Exception) {
//                    withContext(Dispatchers.Main) {
//                        _error.value = e.message
//                    }
//                } finally {
//                    withContext(Dispatchers.Main) {
//                        _loading.value = false
//                    }
//                }
//            } else {
//                withContext(Dispatchers.Main) {
//                    _error.value = "username atau password tidak valid!"
//                    _loading.value = false
//                }
//            }
        }
    }

    fun saveToken(token: String) {
        viewModelScope.launch(Dispatchers.Main) {
            saveTokenUseCase.invoke(token)
            //authRepository.saveToken(token)
        }
    }

    fun checkLogin() {
        viewModelScope.launch(Dispatchers.IO) {
            checkLoginUseCase.invoke().collectLatest { isLoggedIn ->
            //authRepository.isLoggedIn().collectLatest { isLoggedIn ->
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