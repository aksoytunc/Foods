package com.tuncaksoy.inviobitirmeprojesi.data.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var userId: String? = "",
    var userEmail: String? = "",
    var userBalance: Int? = 0,
    var ppUrl: String? = ""
) {
}