package com.tuncaksoy.inviobitirmeprojesi.retrofit

import android.content.Context
import android.net.*
import android.os.Build
import androidx.lifecycle.MutableLiveData

class NetworkConnection(val context: Context) : MutableLiveData<Boolean>() {
    var connectionManger: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    lateinit var netwrokCallback: ConnectivityManager.NetworkCallback

    override fun onActive() {
        super.onActive()
        updateConnection()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                connectionManger.registerDefaultNetworkCallback(NetworkConnectioncallback())
            }
            else -> {
                lollipopNetworkRequest()
            }
        }
    }

    private fun lollipopNetworkRequest() {
        val requestBuilder = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        connectionManger.registerNetworkCallback(
            requestBuilder.build(),
            NetworkConnectioncallback()
        )
    }

    private fun NetworkConnectioncallback(): ConnectivityManager.NetworkCallback {
        netwrokCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                postValue(false)
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                postValue(true)
            }
        }
        return netwrokCallback
    }

    private fun updateConnection() {
        val activeNetwork: NetworkInfo? = connectionManger.activeNetworkInfo
        postValue((activeNetwork?.isConnected == true))
    }
}