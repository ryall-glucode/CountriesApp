package com.example.countriesapp.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "countries", indices = [Index(value = ["name"], unique= true)])
data class CountryStore(
    @PrimaryKey(autoGenerate = false)
    val countryCode: String,
    val name: String,
    val flag: String?,
    val languages: List<String>,
    val lat: Double,
    val lon: Double,
    val isFavourite: Boolean
)
