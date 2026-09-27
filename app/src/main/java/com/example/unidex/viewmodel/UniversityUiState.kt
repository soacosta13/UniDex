package com.example.unidex.viewmodel

import android.os.Message
import com.example.unidex.model.University

//sealed class representing every possible state of the search screen. mainactivity reacts to each state through a "when" block
sealed class UniversityUiState {
    object  Idle : UniversityUiState()
    object Loading : UniversityUiState()
    data class  Success(val universities: List<University>) : UniversityUiState()
    object Empty : UniversityUiState()
    data class  Error(val code: Int?, val message: String?) : UniversityUiState()
}