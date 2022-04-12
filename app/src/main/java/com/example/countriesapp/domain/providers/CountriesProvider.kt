package com.example.countriesapp.domain.providers

import com.example.countriesapp.domain.models.Country
import com.example.countriesapp.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface CountriesProvider {
    val countries: Flow<List<Country>>
    val favouriteCountries: Flow<List<Country>>
}

class CountriesProviderImpl @Inject internal constructor(
    repo: CountryRepository
): CountriesProvider {
    override val countries: Flow<List<Country>> = repo.countries.map { stores ->
        stores.map { Country(it) }
    }

    override val favouriteCountries: Flow<List<Country>> = countries.map { countries ->
        countries.filter { it.isFavourite }
    }
}