package com.example.countriesapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.countriesapp.databinding.CustomListBinding
import com.example.countriesapp.viewmodel.CountryViewData

class RvCountryAdapter(private val listener: OnCountryClickListener, var countries: List<CountryViewData> = listOf()) :
    RecyclerView.Adapter<RvCountryAdapter.ViewHolder>() {

    fun update(listData: List<CountryViewData>) {
        this.countries = listData
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: CustomListBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            this.itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION){
                val clickedCountry = countries[position]
                listener.onCountryClick(clickedCountry.countryCode)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CustomListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(countries[position]) {
                binding.tvCountryName.text = this.name
                Glide.with(binding.imageview).load(this.flag)
                    .into(holder.binding.imageview)
            }
        }
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    interface OnCountryClickListener {
        fun onCountryClick(code: String)
    }
}
