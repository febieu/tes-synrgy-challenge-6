package com.anangkur.synrgychapter6.domain.usecase

import kotlinx.coroutines.flow.Flow

interface LoginUseCase {
    suspend fun authenticate(username: String, password: String) : String

    suspend fun saveToken(token: String)

    suspend fun checkLogin(): Flow<Boolean?>
}