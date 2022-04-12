package com.example.countriesapp.api

import com.example.countriesapp.model.CountryResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/v3.1/all")
    suspend fun getCountries(): Response<List<CountryResponse>>
}