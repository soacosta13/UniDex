package com.example.unidex.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.unidex.R
import  com.example.unidex.databinding.ActivityMainBinding
import com.example.unidex.model.University
import com.example.unidex.view.adapter.UniversityAdapter
import com.example.unidex.viewmodel.UniversityUiState
import com.example.unidex.viewmodel.UniversityViewModel
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: UniversityViewModel
    private lateinit var adapter: UniversityAdapter

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

            when (state) {
                is UniversityUiState.Idle -> {
                    binding.recyclerView.visibility = View.GONE
                }
                is UniversityUiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
                is UniversityUiState.Success -> {
                    binding.recyclerView.visibility = View.VISIBLE
                    adapter.updateItems(state.universities)
                }
                is UniversityUiState.Empty -> {
                    binding.recyclerView.visibility = View.GONE
                    binding.tvMessage.visibility = View.VISIBLE
                    binding.tvMessage.text = getString(R.string.msg_empty_results)
                }
                is UniversityUiState.Error -> {
                    binding.recyclerView.visibility = View.GONE
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
        binding.btnSearch.setOnClickListener {
            val country = binding.etCountry.text.toString().trim()
            if (country.isNotEmpty()) {
                viewModel.searchUniversities(country)
            } else {
                Toast.makeText(this, getString(R.string.msg_empty_input), Toast.LENGTH_SHORT).show()
            }
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