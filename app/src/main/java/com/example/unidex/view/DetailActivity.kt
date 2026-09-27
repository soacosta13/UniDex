package com.example.unidex.view

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.unidex.R
import com.example.unidex.model.University

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_UNIVERSITY = "extra_university"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //only add the fragment the firs time this activity is created. prevents duplicated fragments
        if (savedInstanceState == null) {
            val university = intent.getParcelableExtra<University>(EXTRA_UNIVERSITY)
                ?: return finish()

            val fragment =UniversityDetailFragment.newInstance(university)

            supportFragmentManager.beginTransaction()
                .replace(R.id.detailContainer, fragment)
                .commit()
        }
    }
}