package com.tuncaksoy.inviobitirmeprojesi.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun makeToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun makeAlert(context: Context, title: String, message: String) {
    android.app.AlertDialog.Builder(context).setTitle(title).setMessage(message).create().show()
}

