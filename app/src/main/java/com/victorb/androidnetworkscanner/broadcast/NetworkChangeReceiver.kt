//package com.victorb.androidnetworkscanner.broadcast
//
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import com.victorb.androidnetworkscanner.extension.NetworkUtil
//
//class NetworkChangeReceiver(val onNetworkChange: (status: Int) -> Unit) : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent) {
//        val status: Int = NetworkUtil.getConnectivityStatusString(context)
//        if ("android.net.conn.CONNECTIVITY_CHANGE" == intent.action) {
//            onNetworkChange.invoke(status)
//        }
//    }
//}