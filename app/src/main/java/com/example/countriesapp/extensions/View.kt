package com.example.countriesapp.extensions

import android.view.View

fun View.hide(keepSpace: Boolean = false) {
    visibility = if (keepSpace) View.INVISIBLE else View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.visibleOrGone(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.visibleOrInvisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}