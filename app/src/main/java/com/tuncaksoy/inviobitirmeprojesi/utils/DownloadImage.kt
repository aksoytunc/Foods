package com.tuncaksoy.inviobitirmeprojesi.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.tuncaksoy.inviobitirmeprojesi.R

fun ImageView.downloadImage(url: String?) {
    if (url == "none"){
       this.setImageResource(R.drawable.user_image)
    }else Glide.with(context).load(url).into(this)
}

@BindingAdapter("android:downloadImage")
fun downloadImageFromUrl(view: ImageView, url: String?) {
        view.downloadImage(url)
}