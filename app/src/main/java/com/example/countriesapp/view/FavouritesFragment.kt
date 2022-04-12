package com.example.countriesapp.view

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
import com.example.countriesapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment(R.layout.fragment_favourites),
    RvCountryAdapter.OnCountryClickListener {

    private lateinit var binding: FragmentFavouritesBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: RvCountryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouritesBinding.bind(view)

        initRecyclerView()
        observeViewData()
    }

    private fun initRecyclerView(){
        adapter = RvCountryAdapter(this)
        binding.rvFavourites.adapter = adapter
        binding.rvFavourites.onSwipe {
            showDialog(it)
        }
    }

    private fun observeViewData() {
        viewModel.favouriteCountriesViewData.observe(viewLifecycleOwner) {
            adapter.update(it)
        }
    }

    private fun showDialog(viewHolder: RecyclerView.ViewHolder){
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Delete Item")
        builder.setMessage("Are you sure you want to delete country?")
        builder.setPositiveButton("Confirm"){dialog, which->
            val position = viewHolder.absoluteAdapterPosition
//            val item = countryList[position]
//            countryList.removeAt(position)
//            viewModel.favourite(item)
//            adapter.notifyItemRemoved(position)
//            Snackbar.make(this.requireView(), "Country removed from favourites", Snackbar.LENGTH_LONG).show()
            val country = adapter.countries[position]
            viewModel.favourite(country.countryCode, false)
        }
        builder.setNegativeButton("Cancel"){dialog, which->
            val position =  viewHolder.absoluteAdapterPosition
            adapter.notifyItemChanged(position)
        }
        builder.show()
    }

    override fun onCountryClick(code: String) {
        viewModel.selectCountry(code)
        (activity as? MainActivity)?.selectTab(0)
    }

    companion object {
        fun newInstance() = FavouritesFragment()
    }
}