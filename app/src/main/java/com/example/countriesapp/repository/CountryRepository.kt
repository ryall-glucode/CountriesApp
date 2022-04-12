package com.example.countriesapp.repository

import androidx.lifecycle.LiveData
import com.example.countriesapp.api.ApiService
import com.example.countriesapp.db.AppDao
import com.example.countriesapp.db.entities.CountryData
import com.example.countriesapp.viewmodel.CountryViewData

class CountryRepository constructor(private val apiService: ApiService,
                                    private val appDao: AppDao) {

    suspend fun getCountries() = apiService.getCountries()

    //suspend fun getCountryByName(name: String) = apiService.getCountryByName(name)

    fun insertCountryRecord(countryViewData: CountryViewData?) {
        appDao.insertRecords(countryViewData)
    }

    fun getAllRecords(): LiveData<MutableList<CountryViewData>?> {
        return appDao.getAllRecords()
    }

    fun deleteCountryRecord(countryViewData: CountryViewData){
        appDao.deleteCountryById(countryViewData)
    }

    fun deleteAllRecords(){
        appDao.deleteAllCountries()
    }
}