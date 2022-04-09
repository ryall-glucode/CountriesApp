package com.example.countriesapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.countriesapp.db.entities.CountryData
import com.example.countriesapp.viewmodel.CountryViewData

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecords(countryViewData: CountryViewData?)

    @Query("SELECT * FROM countries")
    fun getAllRecords(): LiveData<MutableList<CountryViewData>?>

    @Delete
    fun deleteCountryById( countryViewData: CountryViewData)

    @Query("DELETE from countries")
    fun deleteAllCountries()



}