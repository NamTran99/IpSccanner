package com.victorb.androidnetworkscanner.data.local.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.victorb.androidnetworkscanner.core.base.recyclerview.KeyModel

data class OptionDrawerModel (
    @DrawableRes val icon: Int,
    val name: String,
    var isSelected: Boolean = false
): KeyModel{
    override val identity: String
        get() = name

}