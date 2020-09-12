package dev.shreyaspatil.datastore.example.preference

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

enum class UiMode {
    LIGHT, DARK
}

class SettingsManager(context: Context) {

    private val dataStore = context.createDataStore(name = "settings_pref")

    val uiModeFlow: Flow<UiMode> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preference ->
            val isDarkMode = preference[IS_DARK_MODE] ?: false

            when (isDarkMode) {
                true -> UiMode.DARK
                false -> UiMode.LIGHT
            }
        }

    suspend fun setUiMode(uiMode: UiMode) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = when (uiMode) {
                UiMode.LIGHT -> false
                UiMode.DARK -> true
            }
        }
    }

    companion object {
        val IS_DARK_MODE = preferencesKey<Boolean>("dark_mode")
    }
}