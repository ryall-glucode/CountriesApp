package com.example.countriesapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.countriesapp.databinding.CustomListBinding
import com.example.countriesapp.viewmodel.CountryViewData

class RvCountryAdapter(private val listener: OnCountryClickListener) :
    RecyclerView.Adapter<RvCountryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CustomListBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            this.itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION){
                val clickedCountry = listData?.get(position) ?: return
                listener.onCountryClick(clickedCountry.countryCode)
            }
        }
    }

    private var listData: MutableList<CountryViewData>? = null

    fun setListData(listData: MutableList<CountryViewData>?) {
        this.listData = listData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CustomListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(listData!![position]) {
                binding.tvCountryName.text = this.name
                Glide.with(binding.imageview).load(this.flag)
                    .into(holder.binding.imageview)
            }
        }
    }

    override fun getItemCount(): Int {
        return listData!!.size
    }

    interface OnCountryClickListener {
        fun onCountryClick(code: String)
    }
}
