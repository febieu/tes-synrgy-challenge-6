package com.anangkur.synrgychapter6.domain.repository

import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun validateInput(username: String, password: String): Boolean
    suspend fun authenticate(username: String, password: String): String
    suspend fun saveToken(token: String)
    suspend fun isLoggedIn(): Flow<Boolean?>
}