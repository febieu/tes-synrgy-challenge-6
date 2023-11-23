package com.anangkur.synrgychapter6.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(
    private val context: Context,
) {

    companion object {

        private val KEY_TOKEN = stringPreferencesKey("token")
        private val KEY_USER_NAME = stringPreferencesKey("username")
        private val KEY_EMAIL = stringPreferencesKey("email")
        private val KEY_PROFILE_PHOTO = stringPreferencesKey("profilePhoto")
        private const val PREF_NAME = "sharedPref"

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREF_NAME)
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { settings -> settings[KEY_TOKEN] = token }
    }

    suspend fun loadToken(): Flow<String?> {
        return context.dataStore.data.map { preferences -> preferences[KEY_TOKEN] }
    }

    suspend fun saveUsername(username: String) {
        context.dataStore.edit { settings -> settings[KEY_USER_NAME] = username }
    }

    suspend fun loadUsername(): Flow<String?> {
        return context.dataStore.data.map { preferences -> preferences[KEY_USER_NAME] }
    }

    suspend fun saveEmail(username: String) {
        context.dataStore.edit { settings -> settings[KEY_EMAIL] = username }
    }

    suspend fun loadEmail(): Flow<String?> {
        return context.dataStore.data.map { preferences -> preferences[KEY_EMAIL] }
    }

    suspend fun deleteUsername() {
        context.dataStore.edit { settings -> settings.remove(KEY_USER_NAME) }
    }

    suspend fun deleteEmail() {
        context.dataStore.edit { settings -> settings.remove(KEY_EMAIL) }
    }

    suspend fun deleteToken() {
        context.dataStore.edit { settings -> settings.remove(KEY_TOKEN) }
    }
    suspend fun saveProfilePhoto(profilePhoto: String) {
        context.dataStore.edit { settings -> settings[KEY_PROFILE_PHOTO] = profilePhoto }
    }

    suspend fun loadProfilePhoto(): Flow<String?> {
        return context.dataStore.data.map { preferences -> preferences[KEY_PROFILE_PHOTO] }
    }
}