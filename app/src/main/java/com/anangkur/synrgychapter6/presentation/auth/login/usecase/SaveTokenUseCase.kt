package com.anangkur.synrgychapter6.presentation.auth.login.usecase

import com.anangkur.synrgychapter6.domain.repository.AuthRepository

class SaveTokenUseCase (
    private val authRepository: AuthRepository,
){
   suspend operator fun invoke(token: String) {
        authRepository.saveToken(token)
    }
}