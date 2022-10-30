package com.tuncaksoy.inviobitirmeprojesi.data.Preferences

import android.content.Context
import com.tuncaksoy.inviobitirmeprojesi.data.model.DisplayData
import com.tuncaksoy.inviobitirmeprojesi.data.model.User


class AppSharedPreferences(val context: Context) {
    val sharedPreferences: android.content.SharedPreferences =
        context.getSharedPreferences("FOOD", Context.MODE_PRIVATE)

    companion object {
        val LANGUAGE_MODE = "languageMode"
        val DISPLAY_MODE = "displayMode"
    }

    fun loadModePreferences(languageMode: Boolean, displayMode: Boolean) {
        sharedPreferences.edit().putBoolean(LANGUAGE_MODE, languageMode).apply()
        sharedPreferences.edit().putBoolean(DISPLAY_MODE, displayMode).apply()
    }

    fun getModePrefences(): DisplayData =
        DisplayData(
            sharedPreferences.getBoolean(LANGUAGE_MODE, false),
            sharedPreferences.getBoolean(DISPLAY_MODE, false)
        )
}