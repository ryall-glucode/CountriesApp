package com.example.countriesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.countriesapp.domain.models.Country
import com.example.countriesapp.domain.providers.CountriesProvider
import com.example.countriesapp.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    private val repository: CountryRepository,
    private val provider: CountriesProvider
) : ViewModel() {
    private val selectedCountryCode = MutableStateFlow<String?>(null)

    val selectedCountryViewData: LiveData<CountryViewData?> =
        combine(provider.countries, selectedCountryCode) { countries, selectedCountryCode ->
            val country = countries.firstOrNull { it.countryCode == selectedCountryCode }
                ?: return@combine null
            CountryViewData(country)
        }.asLiveData()

    val favouriteCountriesViewData: LiveData<List<CountryViewData>> = provider.favouriteCountries.map { countries ->
        countries.map { CountryViewData(it) }
    }.asLiveData()

    init {
        getCountries()
    }

    //TODO - Componentize fetching logic
    private fun getCountries() = viewModelScope.launch {
        repository.refreshCountries()
        selectedCountryCode.value = provider.countries.firstOrNull()?.random()?.countryCode
    }

    fun selectCountry(code: String) {
        selectedCountryCode.value = code
    }

    fun selectCountryByName(name: String) {
        viewModelScope.launch {
            val countries = provider.countries.firstOrNull()
            val selectedCountry = countries?.firstOrNull { it.name.contains(name, true) }
            selectedCountryCode.value = selectedCountry?.countryCode
        }
    }

    fun favourite(code: String, isFavourite: Boolean) {
        viewModelScope.launch {
            val countries = provider.countries.firstOrNull()
            val country = countries?.firstOrNull { it.countryCode == code } ?: return@launch
            val countryStore = country.copy(isFavourite = isFavourite).toStore()
            repository.insert(countryStore)
        }
    }
}

data class CountryViewData(
    val name: String,
    val countryCode: String,
    val flag: String?,
    val isFavourite: Boolean,
    val languages: List<String>,
    val lat: Double,
    val lon: Double
) {
    constructor(country: Country): this(
        name = country.name,
        countryCode = country.countryCode,
        flag = country.flagUrl,
        isFavourite = country.isFavourite,
        languages = country.languages,
        lat = country.lat,
        lon = country.lon
    )
}