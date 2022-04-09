package com.example.countriesapp.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.countriesapp.R
import com.example.countriesapp.adapters.RvCountryAdapter
import com.example.countriesapp.adapters.RvLanguageAdapter
import com.example.countriesapp.databinding.FragmentFavouritesBinding
import com.example.countriesapp.db.entities.CountryData
import com.example.countriesapp.viewmodel.CountryViewData
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

    fun initRecyclerView(){
        binding.rvFavourites.layoutManager = LinearLayoutManager(activity)
        rvCountryAdapter = RvCountryAdapter(this)

        val itemSwipe = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showDialog(viewHolder)
            }
        }
        val swap = ItemTouchHelper(itemSwipe)
        swap.attachToRecyclerView(binding.rvFavourites)
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

    override fun onItemClick(position: Int) {
        val item = countryList[position]
           Toast.makeText(requireContext(), "Chosen: ${item.name}", Toast.LENGTH_SHORT).show()
    }
}