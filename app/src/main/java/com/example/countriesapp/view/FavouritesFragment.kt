package com.example.countriesapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.countriesapp.R
import com.example.countriesapp.adapters.RvCountryAdapter
import com.example.countriesapp.databinding.FragmentFavouritesBinding
import com.example.countriesapp.db.entities.CountryData
import com.example.countriesapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.net.ssl.SSLHandshakeException

@AndroidEntryPoint
class FavouritesFragment : Fragment(R.layout.fragment_favourites),
    RvCountryAdapter.OnItemClickListener {

    private lateinit var binding: FragmentFavouritesBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var rvCountryAdapter: RvCountryAdapter

    private var countryList = mutableListOf<CountryData>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouritesBinding.bind(view)

        getAllFavourites()
    }

    override fun onResume() {
        super.onResume()
        binding.progressbar.visibility = View.VISIBLE
        getAllFavourites()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getAllFavourites() {
        try {
            viewModel.getAllRecords().observe(this) { favouriteCountries ->
                if (favouriteCountries.isNotEmpty()) {
                    for (country in favouriteCountries) {
                        if (!countryList.contains(country)) {
                            countryList.add(country)
                        }
                    }
                    rvCountryAdapter = RvCountryAdapter(this@FavouritesFragment)
                    rvCountryAdapter.setListData(countryList)
                    rvCountryAdapter.notifyDataSetChanged()

                    binding.rvFavourites.apply {
                        adapter = rvCountryAdapter
                        layoutManager = layoutManager
                    }
                }
                binding.progressbar.visibility = View.GONE
            }
        }catch (e: SSLHandshakeException){
            e.printStackTrace()
        }
    }

    companion object {
        fun newInstance() = FavouritesFragment()
    }

    override fun onItemClick(position: Int) {
        val item = countryList[position]

        var list: Any

        val languagesResponse = item?.languages

        list = languagesResponse?.map {
            it.value
        }!!

           Toast.makeText(requireContext(), "Chosen: ${item.name}", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemDeleteClick(position: Int) {
        val item = countryList[position]
        viewModel.deleteCountryRecord(item)
        rvCountryAdapter.notifyDataSetChanged()
        Toast.makeText(requireContext(), "Deleted: ${item.name}", Toast.LENGTH_SHORT).show()
    }
}