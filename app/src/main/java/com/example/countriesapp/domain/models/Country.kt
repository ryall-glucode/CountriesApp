package com.example.countriesapp.domain.models

import com.example.countriesapp.db.entities.CountryStore
import com.example.countriesapp.model.CountryResponse

data class Country(
    val name: String,
    val countryCode: String,
    val flagUrl: String?,
    val languages: List<String>,
    val lat: Double,
    val lon: Double,
    val isFavourite: Boolean = false
) {
    constructor(store: CountryStore): this(
        name = store.name,
        countryCode = store.countryCode,
        flagUrl = store.flag,
        languages = store.languages,
        lat = store.lat,
        lon = store.lon,
        isFavourite = store.isFavourite
    )

    constructor(response: CountryResponse): this(
        name = response.name.common,
        countryCode = response.cioc,
        flagUrl = response.flags.png,
        languages = response.languages.map { it.value },
        lat = response.latlng.first(),
        lon = response.latlng.last()
    )

    fun toStore(): CountryStore = CountryStore(
        name = name,
        countryCode = countryCode,
        flag = flagUrl,
        languages = languages,
        lat = lat,
        lon = lon,
        isFavourite = isFavourite
    )
}