package com.example.countriesapp.api

import com.example.countriesapp.model.CountryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/v3.1/all")
    suspend fun getCountries(): Response<List<CountryResponse>>

    @GET("/v3.1/name/{name}")
    suspend fun getCountryByName(
        @Path("name") name: String?
    ): Response<List<CountryResponse?>>
}