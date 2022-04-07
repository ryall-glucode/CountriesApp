package com.example.countriesapp.repository

import androidx.lifecycle.LiveData
import com.example.countriesapp.api.ApiService
import com.example.countriesapp.db.AppDao
import com.example.countriesapp.db.entities.CountryData

class CountryRepository constructor(private val apiService: ApiService,
                                    private val appDao: AppDao) {

    suspend fun getCountries() = apiService.getCountries()

    suspend fun getCountryByName(name: String) = apiService.getCountryByName(name)

    fun insertCountryRecord(countryData: CountryData) {
        appDao.insertRecords(countryData)
    }

    fun getAllRecords(): LiveData<MutableList<CountryData>> {
        return appDao.getAllRecords()
    }

    fun deleteCountryRecord(countryData: CountryData){
        appDao.deleteCountryById(countryData)
    }

    fun deleteAllRecords(){
        appDao.deleteAllCountries()
    }
}