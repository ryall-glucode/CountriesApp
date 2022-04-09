package com.example.countriesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.countriesapp.db.entities.CountryData
import com.example.countriesapp.model.CountryResponse
import com.example.countriesapp.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(private val repository: CountryRepository) : ViewModel() {

    private val _uiCountryState = MutableStateFlow<CountryUiState>(CountryUiState.Empty)
    val uiCountryState: StateFlow<CountryUiState> = _uiCountryState

    private val _uiCountryByNameState =
        MutableStateFlow<CountryByNameUiState>(CountryByNameUiState.Empty)
    val uiCountryByNameState: StateFlow<CountryByNameUiState> = _uiCountryByNameState

    private val countries = MutableStateFlow<List<CountryResponse>?>(null)
    private val selectedCountryCode = MutableStateFlow<String?>(null)

    val countryViewData: LiveData<CountryViewData?> =
        combine(countries, selectedCountryCode) { countries, selectedCountryCode ->
            val country = countries?.firstOrNull { it.cioc == selectedCountryCode } ?: return@combine null
            val languages = country.languages.map {
                it.value
            }
            val flag = country.flags.png
            CountryViewData(country.name.common, flag, languages)
        }.asLiveData()

    init {
        _uiCountryState.value = CountryUiState.Loading
        getCountries()
    }

    //TODO - Componentize fetching logic
    private fun getCountries() = viewModelScope.launch {
        _uiCountryState.value = CountryUiState.Loading
        repository.getCountries().let { response ->
            if (response.isSuccessful) {
                val countriesResponse = response.body()
           //     _uiCountryState.value = CountryUiState.Success(response.body())
                selectedCountryCode.value = countriesResponse?.random()?.cioc
                countries.value = countriesResponse
            } else {
         //       _uiCountryState.value = CountryUiState.Error(response.message())
            }
        }
    }

    fun selectCountry(code: String) {
        selectedCountryCode.value = code
    }

    fun selectCountryByName(name: String) {
        val selectedCountry = countries.value?.firstOrNull { it.name.common == name }
        selectedCountryCode.value = selectedCountry?.cioc
    }

    fun getAllRecords(): LiveData<MutableList<CountryViewData>?> {
        return repository.getAllRecords()
    }

    fun insertCountryRecord(countryViewData: CountryViewData?) {
        return repository.insertCountryRecord(countryViewData)
    }

    fun deleteCountryRecord(countryViewData: CountryViewData) {
        return repository.deleteCountryRecord(countryViewData)
    }

    fun deleteAllRecords() {
        return repository.deleteAllRecords()
    }

    sealed class CountryUiState {
        data class Success(val country: List<CountryResponse?>?) : CountryUiState()
        data class Error(val message: String) : CountryUiState()
        object Loading : CountryUiState()
        object Empty : CountryUiState()
    }

    sealed class CountryByNameUiState {
        data class Success(val country: List<CountryResponse?>?) : CountryByNameUiState()
        data class Error(val message: String) : CountryByNameUiState()
        object Loading : CountryByNameUiState()
        object Empty : CountryByNameUiState()
    }
}

@Entity(
    tableName = "countries"
)
data class CountryViewData(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val flag: String,
    val languages: List<String>)