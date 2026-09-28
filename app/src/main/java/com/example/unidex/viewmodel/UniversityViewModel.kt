package com.example.unidex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unidex.repository.UniversityRepository
import kotlinx.coroutines.launch

class UniversityViewModel : ViewModel() {
    private val repository = UniversityRepository()

    private val _uiState = MutableLiveData<UniversityUiState>(UniversityUiState.Idle)
    val uiState: LiveData<UniversityUiState> get() = _uiState

    fun searchUniversities(country: String) {
        if (country.isBlank()) return

        //viewmodelscope cancels the coroutine if the viewmodel is destroyed
        viewModelScope.launch {
            _uiState.value = UniversityUiState.Loading

            when (val result = repository.searchUniversities(country)) {
                is UniversityRepository.RepoResult.Success -> {
                    _uiState.value = if (result.data.isEmpty()) {
                        UniversityUiState.Empty
                    } else {
                        UniversityUiState.Success(result.data)
                    }
                }
                is UniversityRepository.RepoResult.HttpError -> {
                    _uiState.value = UniversityUiState.Error(code = result.code, message = null)
                }
                is UniversityRepository.RepoResult.NetworkError ->  {
                    _uiState.value = UniversityUiState.Error(code = null, message = result.message)
                }
            }
        }
    }

    fun resetToIdle() {
        _uiState.value = UniversityUiState.Idle
    }
}