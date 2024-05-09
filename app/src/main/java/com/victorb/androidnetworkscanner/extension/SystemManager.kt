package com.victorb.androidnetworkscanner.extension

import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import android.telephony.TelephonyManager
import com.victorb.androidnetworkscanner.core.MyApplication

object SystemManager {
    fun getCountryCode(): String {
        val context = MyApplication.application
        val telephonyManager = context.getSystemService(TelephonyManager::class.java)
        return telephonyManager.networkCountryIso
    }

    fun registerCheckConnectivityStatus(callback: ConnectivityManager.NetworkCallback) {
        val context = MyApplication.application
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        connectivityManager.registerDefaultNetworkCallback(callback)
    }

    fun getWifiInfo(): WifiInfo? {
        val context = MyApplication.application
        return context.getSystemService(WifiManager::class.java).connectionInfo
    }
}