package com.example.countriesapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.countriesapp.db.entities.CountryData

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecords(countryData: CountryData)

    @Query("SELECT * FROM countries")
    fun getAllRecords(): LiveData<MutableList<CountryData>>

    @Delete
    fun deleteCountryById(countryData: CountryData)

    @Query("DELETE from countries")
    fun deleteAllCountries()



}