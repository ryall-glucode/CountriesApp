package com.example.countriesapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.countriesapp.databinding.CustomListBinding
import com.example.countriesapp.db.entities.CountryData

class RvCountryAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<RvCountryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CustomListBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            this.itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    private var listData: MutableList<CountryData>? = null

    fun setListData(listData: MutableList<CountryData>?) {
        this.listData = listData
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
                binding.ivDelete.setOnClickListener {
                   listener.onItemDeleteClick(position)
                    listData?.removeAt(position)
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listData!!.size
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onItemDeleteClick(position: Int)
    }
}
