package com.anangkur.synrgychapter6.presentation.auth.login.usecase

import com.anangkur.synrgychapter6.domain.repository.AuthRepository

//buat baru per kelas2 dari function dbwh, 01:15:01
//gak jadi pake usecase di domain

class AuthenticateUseCase (
    private val authRepository: AuthRepository,
){
    suspend operator fun invoke(
        username: String, password: String
    ): String {
        if (authRepository.validateInput(username, password)){
            return authRepository.authenticate(username, password)
        }
        else{
            throw UnsupportedOperationException("username atau password tidak valid!")
        }
    }





}