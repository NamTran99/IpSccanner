package com.victorb.androidnetworkscanner

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Device(
        val ip: String?,
        val hostname: String?
): Parcelable