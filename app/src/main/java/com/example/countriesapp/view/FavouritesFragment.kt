package com.example.countriesapp.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.countriesapp.R
import com.example.countriesapp.adapters.RvCountryAdapter
import com.example.countriesapp.databinding.FragmentFavouritesBinding
import com.example.countriesapp.extensions.onSwipe
import com.example.countriesapp.viewmodel.CountryViewData
import com.example.countriesapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment(R.layout.fragment_favourites),
    RvCountryAdapter.OnCountryClickListener {

    private lateinit var binding: FragmentFavouritesBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var rvCountryAdapter: RvCountryAdapter

    private var countryList = mutableListOf<CountryViewData>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouritesBinding.bind(view)

        getAllFavourites()
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        binding.progressbar.visibility = View.VISIBLE
        getAllFavourites()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getAllFavourites() {
            viewModel.getAllRecords().observe(this) { favouriteCountries ->
                if (!favouriteCountries.isNullOrEmpty()) {
                    for (country in favouriteCountries){
                        if (!countryList.contains(country)){
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
    }

    companion object {
        fun newInstance() = FavouritesFragment()
    }

    private fun initRecyclerView(){
        rvCountryAdapter = RvCountryAdapter(this)
        binding.rvFavourites.onSwipe {
            showDialog(it)
        }
    }

    private fun showDialog(viewHolder: RecyclerView.ViewHolder){
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Delete Item")
        builder.setMessage("Are you sure you want to delete item?")
        builder.setPositiveButton("Confirm"){dialog, which->
            val position = viewHolder.absoluteAdapterPosition
            val item = countryList[position]
            countryList.removeAt(position)
            viewModel.deleteCountryRecord(item)
            rvCountryAdapter.notifyItemRemoved(position)
        }
        builder.setNegativeButton("Cancel"){dialog, which->
            val position =  viewHolder.absoluteAdapterPosition
            rvCountryAdapter.notifyItemChanged(position)
        }
        builder.show()
    }

    override fun onCountryClick(code: String) {
        viewModel.selectCountry(code)
        (activity as? MainActivity)?.selectTab(0)
    }
}