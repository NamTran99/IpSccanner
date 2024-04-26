package com.victorb.androidnetworkscanner

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

data class Device(
        val ip: String?,
        val hostname: String?
): Serializable