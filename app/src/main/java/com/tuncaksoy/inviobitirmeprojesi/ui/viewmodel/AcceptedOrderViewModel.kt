package com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.tuncaksoy.inviobitirmeprojesi.retrofit.NetworkConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AcceptedOrderViewModel@Inject constructor(
    var networkConnection: NetworkConnection
):ViewModel() {
}