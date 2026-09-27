package com.example.unidex

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.unidex.util.ThemePreferences

class UniDexApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val darkModeEnabled = ThemePreferences.isDarkModeEnabled(this)
        AppCompatDelegate.setDefaultNightMode(
            if (darkModeEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
