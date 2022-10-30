package com.tuncaksoy.inviobitirmeprojesi.data.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var userId: String? = null,
    var userEmail: String? = null,
    var userBalance: Int? = null,
    var ppUrl: String? = null
) {
}