package com.victorb.androidnetworkscanner

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import java.io.IOException
import java.net.Inet4Address
import java.net.InetAddress
import java.net.InterfaceAddress
import java.net.NetworkInterface
import java.util.Locale


fun isWifiEnabled(context: Context): Boolean =
    (context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager).isWifiEnabled

fun isWifiConnected(context: Context): Boolean =
    (context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetwork != null

fun getPhoneIp(context: Context): Int =
    (context.getSystemService(Context.WIFI_SERVICE) as WifiManager).dhcpInfo.ipAddress

fun getNetworkPrefixLength(context: Context): Int {
    // IP object
    val inetAddress: InetAddress = InetAddress.getByAddress(intIpToByteArray(getPhoneIp(context)));

    // Get the network interfaces
    val networkInterface: NetworkInterface = NetworkInterface.getByInetAddress(inetAddress);
    val interfaceAddresses: MutableList<InterfaceAddress> = networkInterface.interfaceAddresses

    // Set the network mask to the one of the first IPv4 interface
    for (address in interfaceAddresses) {
        if (address.address is Inet4Address) {
            return address.networkPrefixLength.toInt()
        }
    }

    return -1
}

fun getIpHostname(ip: Int): String = InetAddress.getByAddress(intIpToByteArray(ip)).hostName

fun isIpReachable(ip: Int): Boolean =
    InetAddress.getByAddress(intIpToByteArray(ip)).isReachable(2000)
//fun isIpReachable1(ip: Int): Boolean = InetAddress.getByAddress(intIpToByteArray(ip)).


fun getLocation(context: Context): Location? {
    try {
        val mLocationManager  =context.getSystemService(LocationManager::class.java)
        val lastKnownLocation = if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        } else {

        }
        mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (lastKnownLocation != null) {
            val latitude = lastKnownLocation.latitude
            val longitude = lastKnownLocation.longitude
            // Use the retrieved latitude and longitude
        } else {
            // Handle no last known location available
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}


fun getCountryCode(): String {
    getLocation()
    return if (isLocationAvailable) {
        val geocoder = Geocoder(mContext, Locale.getDefault())
        // Get the current location from the input parameter list
        // Create a list to contain the result address
        var addresses: List<Address>? = null
        try {
            /*
              * Return 1 address.
              */
            mLatitude = getLatitude()
            mLongitude = getLongitude()
            addresses = geocoder.getFromLocation(mLatitude, mLongitude, 1)
        } catch (e1: IOException) {
            e1.printStackTrace()
            Log.e("returning", "tm")
            //TelephonyManager tm = (TelephonyManager)mContext.getSystemService(mContext.TELEPHONY_SERVICE);
            //return tm.getNetworkCountryIso();
        } catch (e2: IllegalArgumentException) {
            // Error message to post in the log
            val errorString = ("Illegal arguments "
                    + mLatitude.toString() + " , "
                    + mLongitude.toString() + " passed to address service")
            e2.printStackTrace()
            return errorString
        }
        // If the reverse geocode returned an address
        if (addresses != null && addresses.size > 0) {
            // Get the first address
            val address = addresses[0]
            /*
                  * Format the first line of address (if available), city, and
                  * country name.
                  */
            // Return the text
            Log.e("returning", address.countryCode)
            val countrycode: String
            countrycode = if (address.countryCode == null) {
                "null"
            } else {
                address.countryCode
            }
            countrycode
        } else {
            "null"
        }
    } else {
        //String locale = getResources().getConfiguration().locale.getCountry();
        Log.e("returning", "wifi")
        getJSON()
        Log.e("ELSE", wifiCode)
        wifiCode
    }
}
