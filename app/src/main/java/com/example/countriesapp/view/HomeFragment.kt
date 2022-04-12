package com.example.countriesapp.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.countriesapp.R
import com.example.countriesapp.adapters.RvLanguageAdapter
import com.example.countriesapp.databinding.FragmentHomeBinding
import com.example.countriesapp.extensions.visibleOrGone
import com.example.countriesapp.viewmodel.CountryViewData
import com.example.countriesapp.viewmodel.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), OnMapReadyCallback {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var rvLanguageAdapter: RvLanguageAdapter

    private val viewModel: MainViewModel by activityViewModels()

    var isFavourite = false
    private var countryData: CountryViewData? = null
    private lateinit var fab: FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

         fab = binding.fab

        setUpLanguagesList()
        observeSelectedCountry()

        binding.etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.selectCountryByName(v.text.toString())
                v.text = ""
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
                true
            } else {
                false
            }
        }



//        lifecycleScope.launchWhenStarted {
//            viewModel.uiCountryState.collect {
//                when (it) {
//                    is MainViewModel.CountryUiState.Loading -> {
//                        binding.progressbar.visibility = View.VISIBLE
//                    }
//                    is MainViewModel.CountryUiState.Success -> {
//                        val randomCountry = it.country?.random()
//                        countryName = randomCountry?.name?.common ?: ""
//                        flag = randomCountry?.flags?.png ?: ""
//
//                        val languagesResponse =  randomCountry?.languages
//
//                        languages = languagesResponse?.map {
//                            it.value
//                        } ?: listOf()
//
//                /*        countryData = CountryData(null,randomCountry!!.name.common,
//                            randomCountry.flags.png,
//                        randomCountry.languages)*/
//
//                        binding.progressbar.visibility = View.GONE
//
//                        binding.tvCountryName.text = countryName
//                        Glide.with(requireContext())
//                            .load(flag)
//                            .into(binding.ivFlag)
//
//                        binding.tvLanguagesSpoken.visibility = View.VISIBLE
//
//                        rvLanguageAdapter = RvLanguageAdapter()
//                        rvLanguageAdapter.setListData(languages)
//                        rvLanguageAdapter.notifyDataSetChanged()
//
//                        binding.rvLanguages.apply {
//                            adapter = rvLanguageAdapter
//                            layoutManager = layoutManager
//                            setHasFixedSize(true)
//                        }
//                    }
//                    is MainViewModel.CountryUiState.Error -> {
//                        binding.progressbar.visibility = View.GONE
//                        Snackbar.make(view, it.message, Snackbar.LENGTH_LONG).show()
//                    }
//                    else -> Unit
//                }
//            }
//        }
//
        fab.setOnClickListener { view ->
                viewModel.insertCountryRecord(countryData)
                isFavourite = true
                fab.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_favourite_selected))
            Snackbar.make(view, "Added to favourites", Snackbar.LENGTH_LONG).show()
            }
    }

    private fun observeSelectedCountry() {
        viewModel.countryViewData.observe(this) { countryViewData ->
            countryData = countryViewData
            //TODO - Improve loading state management
            //TODO - Create view extension for visible/gone
            binding.progressbar.visibleOrGone(countryViewData == null)
            countryViewData ?: return@observe

            binding.tvCountryName.text = countryViewData.name
            Glide.with(requireContext())
                .load(countryViewData.flag)
                .into(binding.ivFlag)

            binding.tvLanguagesSpoken.visibleOrGone(countryViewData.languages.isNotEmpty())

            rvLanguageAdapter.setListData(countryViewData.languages)

            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(this)
        }
    }

    private fun setUpLanguagesList() = with(binding.rvLanguages) {
        rvLanguageAdapter = RvLanguageAdapter()
        apply {
            adapter = rvLanguageAdapter
            setHasFixedSize(true)
        }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.clear()
        val latLng = LatLng(countryData?.latLng?.firstOrNull()!!.toDouble(),  countryData?.latLng!!.last()!!
            .toDouble())
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 3f))
        googleMap.addMarker(MarkerOptions().position(latLng))
        googleMap.setOnMapClickListener {
        }
    }
}