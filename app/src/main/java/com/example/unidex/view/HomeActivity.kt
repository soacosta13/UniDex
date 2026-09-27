package com.example.unidex.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.unidex.databinding.ActivityHomeBinding
import com.example.unidex.util.ThemePreferences

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDarkModeSwitch()
        setupButtons()
    }

    private fun setupDarkModeSwitch() {
        // Reflect the persisted preference in the switch, without firing the listener below
        binding.switchDarkMode.isChecked = ThemePreferences.isDarkModeEnabled(this)

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            ThemePreferences.setDarkModeEnabled(this, isChecked)
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    private fun setupButtons() {
        binding.btnGoSearch.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.btnExit.setOnClickListener {
            // Closes every Activity in this task, exiting the app completely
            finishAffinity()
        }
    }
}