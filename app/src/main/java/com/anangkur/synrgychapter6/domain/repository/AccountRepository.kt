package com.anangkur.synrgychapter6.domain.repository

import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    suspend fun loadUsername(): Flow<String?>
    suspend fun loadEmail(): Flow<String?>
    suspend fun logout()
    suspend fun loadProfilePhoto(): Flow<String?>
    suspend fun saveProfilePhoto(profilePhoto: String)
}