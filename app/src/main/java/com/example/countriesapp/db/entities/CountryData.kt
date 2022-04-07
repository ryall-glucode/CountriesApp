package com.example.countriesapp.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "countries", indices = [Index(value = ["name"], unique= true)]
)
@Parcelize
data class CountryData(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id: Int? = 0,
    val name:String?,
    val countryCode:String?,
    val flag:String?,
    val languages: Map<String, String>?
    ) :Parcelable
