package com.myrgb.ledcontroller.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

enum class WifiStatus {
    AVAILABLE, UNAVAILABLE, LOST
}

class NetworkConnectivityObserver(
    context: Context
) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun observe(networkRequest: NetworkRequest): Flow<WifiStatus> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch { send(WifiStatus.AVAILABLE) }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch { send(WifiStatus.UNAVAILABLE) }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch { send(WifiStatus.LOST) }
                }
            }
            connectivityManager.registerNetworkCallback(networkRequest, callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }
}