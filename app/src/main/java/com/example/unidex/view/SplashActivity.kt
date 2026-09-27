package com.example.unidex.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.unidex.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    companion object {
        private const val SPLASH_DELAY_MS = 1500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // lifecycleScope automatically cancels this coroutine if the Activity
        // is destroyed before the delay finishes (e.g. the user rotates the screen).
        lifecycleScope.launch {
            delay(SPLASH_DELAY_MS)
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            finish() // Removes SplashActivity from the back stack so "back" doesn't return to it
        }
    }
}