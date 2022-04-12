package com.example.countriesapp.db

import androidx.room.*
import com.example.countriesapp.db.entities.CountryStore
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg country: CountryStore)

    @Query("SELECT * FROM countries")
    fun getAllRecords(): Flow<List<CountryStore>>

    @Delete
    suspend fun deleteCountryById(country: CountryStore)

    @Query("DELETE from countries")
    suspend fun deleteAllCountries()
}