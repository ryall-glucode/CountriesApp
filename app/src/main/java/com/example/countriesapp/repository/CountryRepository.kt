package com.example.countriesapp.repository

import com.example.countriesapp.api.ApiService
import com.example.countriesapp.db.AppDao
import com.example.countriesapp.db.entities.CountryStore
import com.example.countriesapp.domain.models.Country
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CountryRepository @Inject internal constructor(
    private val apiService: ApiService,
    private val appDao: AppDao
) {

    val countries: Flow<List<CountryStore>> = appDao.getAllRecords()

    suspend fun refreshCountries() {
        val response = apiService.getCountries()
        if (response.isSuccessful) {
            val countries = response.body()?.filter { !it.cioc.isNullOrBlank() } ?: return
            val stores = countries.map { Country(it).toStore() }
            insert(*stores.toTypedArray())
        }
    }

    suspend fun insert(vararg country: CountryStore) = appDao.insert(*country)

    suspend fun deleteAllRecords() {
        appDao.deleteAllCountries()
    }
}