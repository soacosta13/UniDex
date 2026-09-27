package com.example.unidex.network

import  com.example.unidex.model.University
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UniversityApiService {
    //get http://universities.hipolabs..... the api returns plain json array, retrofit maps it to List<University>
    @GET("search")
    suspend fun searchByCountry(
        @Query("country") country: String
    ): Response<List<University>>

}