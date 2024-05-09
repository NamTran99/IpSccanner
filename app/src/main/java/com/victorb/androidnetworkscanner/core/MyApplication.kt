package com.victorb.androidnetworkscanner.core

import android.app.Application
import com.blongho.country_data.World


class MyApplication :Application() {

    companion object {
        lateinit var application: Application
    }

    override fun onCreate() {
        super.onCreate()
        World.init(this)
        application = this
    }
}