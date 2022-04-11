package com.example.countriesapp.extensions

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.onSwipe(direction: Int = ItemTouchHelper.RIGHT,  action: (viewHolder: RecyclerView.ViewHolder) -> Unit) {
    val itemSwipe = object : ItemTouchHelper.SimpleCallback(0, direction) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            action(viewHolder)
        }
    }
    val swap = ItemTouchHelper(itemSwipe)
    swap.attachToRecyclerView(this)
}

