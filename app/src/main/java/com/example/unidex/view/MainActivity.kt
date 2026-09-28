package com.example.unidex.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.unidex.R
import  com.example.unidex.databinding.ActivityMainBinding
import com.example.unidex.model.University
import com.example.unidex.view.adapter.UniversityAdapter
import com.example.unidex.viewmodel.UniversityUiState
import com.example.unidex.viewmodel.UniversityViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: UniversityViewModel
    private lateinit var adapter: UniversityAdapter

    // Holds the pending debounced search so we can cancel it if the user keeps typing
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[UniversityViewModel::class.java]

        setupToolbar()
        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupToolbar() {
        binding.toolbarUnidex.btnHome.visibility = View.VISIBLE
        binding.toolbarUnidex.btnHome.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = UniversityAdapter(emptyList()) { selectedUniversity ->
            openDetailScreen(selectedUniversity)
        }
        binding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.uiState.observe(this) { state ->
            //reset visibility on every state change, shows only whats relevant
            binding.progressBar.visibility = View.GONE
            binding.tvMessage.visibility = View.GONE
            binding.recyclerView.visibility = View.GONE
            binding.tvResultsCount.visibility = View.GONE

            when (state) {
                is UniversityUiState.Idle -> {

                }
                is UniversityUiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is UniversityUiState.Success -> {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.tvResultsCount.visibility = View.VISIBLE
                    binding.tvResultsCount.text =
                        getString(R.string.label_results_count, state.universities.size)
                    adapter.updateItems(state.universities)
                }
                is UniversityUiState.Empty -> {
                    binding.tvMessage.visibility = View.VISIBLE
                    binding.tvMessage.text = getString(R.string.msg_empty_results)
                }
                is UniversityUiState.Error -> {
                    val text = if (state.code != null) {
                        getString(R.string.error_server, state.code)
                    } else {
                        getString(R.string.error_connection, state.message ?: "")
                    }
                    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupListeners() {
        binding.etCountry.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnClearSearch.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                val country = s?.toString()?.trim().orEmpty()

                // Cancel any search still waiting to fire, since the user kept typing
                searchJob?.cancel()

                if (country.isEmpty()) {
                    viewModel.resetToIdle()
                    return
                }

                // Debounce: wait 500ms after the user stops typing before hitting the API,
                // so we don't fire a network request on every single keystroke
                searchJob = lifecycleScope.launch {
                    delay(500)
                    viewModel.searchUniversities(country)
                }
            }
        })

        binding.btnClearSearch.setOnClickListener {
            binding.etCountry.text?.clear()
        }
    }

    //navigates to detailactivity, passing de university object as a parcelable extra
    private fun openDetailScreen(university: University) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_UNIVERSITY, university)
        }
        startActivity(intent)
    }
}