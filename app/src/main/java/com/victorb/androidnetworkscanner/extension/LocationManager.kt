package com.victorb.androidnetworkscanner.extension

import android.location.Address
import android.location.Geocoder
import com.victorb.androidnetworkscanner.core.MyApplication
import java.util.Locale


object LocationManager {


    fun getAddress(lat: Double, long: Double): Address?{
        var gc = Geocoder(MyApplication.application, Locale.getDefault())
         return gc.getFromLocation(lat, long, 1)?.get(0)
    }

}


