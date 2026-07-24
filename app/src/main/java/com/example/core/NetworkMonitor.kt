package com.example.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext

class NetworkMonitor(context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun isCurrentlyOnline(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    fun observeNetworkStatus(onStatusChanged: (Boolean) -> Unit): ConnectivityManager.NetworkCallback {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                onStatusChanged(true)
            }

            override fun onLost(network: Network) {
                onStatusChanged(false)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        try {
            connectivityManager.registerNetworkCallback(request, callback)
        } catch (e: Exception) {
            // Fallback for safety
            onStatusChanged(isCurrentlyOnline())
        }

        return callback
    }

    fun unregisterCallback(callback: ConnectivityManager.NetworkCallback) {
        try {
            connectivityManager.unregisterNetworkCallback(callback)
        } catch (e: Exception) {
            // Safe ignore
        }
    }
}

@Composable
fun rememberIsOnlineState(): State<Boolean> {
    val context = LocalContext.current
    return produceState(initialValue = true) {
        val monitor = NetworkMonitor(context)
        value = monitor.isCurrentlyOnline()

        val callback = monitor.observeNetworkStatus { isOnline ->
            value = isOnline
        }

        awaitDispose {
            monitor.unregisterCallback(callback)
        }
    }
}
