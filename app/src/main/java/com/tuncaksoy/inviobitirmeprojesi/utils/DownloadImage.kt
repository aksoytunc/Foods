package com.tuncaksoy.inviobitirmeprojesi.glide

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

fun ImageView.downloadImage(url: String?) {
    Glide.with(context).load(url).into(this)
}

@BindingAdapter("android:downloadImage")
fun dowloadImage(view: ImageView, url: String?) {
    if (url != "" && url != null) {
        view.downloadImage(url)
    }
}