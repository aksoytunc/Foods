package com.tuncaksoy.inviobitirmeprojesi.data.Preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tuncaksoy.inviobitirmeprojesi.data.model.DisplayData
import com.tuncaksoy.inviobitirmeprojesi.data.model.User
import kotlinx.coroutines.flow.first

class AppPref(var context: Context) {
    val Context.ds: DataStore<Preferences> by preferencesDataStore("user")

    companion object {
        val USER_ID = stringPreferencesKey("userId")
        val USER_EMAIL = stringPreferencesKey("userEmail")
        val LANGUAGE_MODE = booleanPreferencesKey("languageMode")
        val DISPLAY_MODE = booleanPreferencesKey("displayMode")
    }

    suspend fun loadUserPreferences(userId: String, userEmail: String) {
        context.ds.edit {
            it[USER_ID] = userId
            it[USER_EMAIL] = userEmail
        }
    }

    suspend fun deleteUserPreferences() {
        context.ds.edit {
            it.remove(USER_ID)
            it.remove(USER_EMAIL)
        }
    }

    suspend fun getUserPreferences(): User {
        val p = context.ds.data.first()
        val user = User(p[USER_ID] ?: "null", p[USER_EMAIL] ?: "null")
        return user
    }

    suspend fun loadModePreferences(languageMode: Boolean, displayMode: Boolean) {
        context.ds.edit {
            it[LANGUAGE_MODE] = languageMode
            it[DISPLAY_MODE] = displayMode
        }
    }

    suspend fun getModePrefences(): DisplayData {
        val p = context.ds.data.first()
        val displayData = DisplayData(p[LANGUAGE_MODE] ?: false, p[DISPLAY_MODE] ?: false)
        return displayData
    }
}