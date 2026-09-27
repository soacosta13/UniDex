package com.example.unidex.repository

import android.util.Log
import com.example.unidex.model.University
import com.example.unidex.network.UniversityApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.Locale

//single source of truth for university data. hides retrofit/network from de viewmodel

class UniversityRepository {

    private val TAG = "UniversityRepository"
    private val BASE_URL = "http://universities.hipolabs.com/"

    private val apiService: UniversityApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UniversityApiService::class.java)

    //result wrapper -> viewmodel distinguish between network failure, server error, successful call
    sealed class RepoResult {
        data class Success(val data: List<University>): RepoResult()
        data class HttpError(val code: Int) : RepoResult()
        data class NetworkError(val message: String) : RepoResult()
    }

    suspend fun searchUniversities(country: String): RepoResult {
        Log.d(TAG, "Searching universities for country: $country")
        return try {
            val response = apiService.searchByCountry(country)
            if (response.isSuccessful) {
                val list = response.body() ?: emptyList()
                Log.d(TAG, "Universities found: ${list.size}")
                RepoResult.Success(list)
            } else {
                Log.e(TAG, "HTTP error: ${response.code()}")
                RepoResult.HttpError(response.code())
            }
        } catch (e: IOException) {
            //thrown when there's no internet connection
            Log.e(TAG, "Network error: ${e.message}")
            RepoResult.NetworkError(e.message ?: "No internet connection")
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error: ${e.message}")
            RepoResult.NetworkError(e.message ?: "Unknown error")
        }
    }
}