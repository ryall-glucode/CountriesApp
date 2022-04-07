package com.example.countriesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.countriesapp.databinding.LanguageCustomListBinding

class RvLanguageAdapter() :
    RecyclerView.Adapter<RvLanguageAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LanguageCustomListBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var listData: List<String>? = null

    fun setListData(listData: List<String>?) {
        this.listData = listData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LanguageCustomListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvLanguage.text = listData!![position]
    }

    override fun getItemCount(): Int {
        return listData!!.size
    }
}