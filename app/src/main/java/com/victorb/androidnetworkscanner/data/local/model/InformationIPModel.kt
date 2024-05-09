package com.victorb.androidnetworkscanner.data.local.model

import androidx.annotation.DrawableRes
import com.marsounjan.icmp4a.Icmp
import com.victorb.androidnetworkscanner.core.base.recyclerview.KeyModel
import java.net.InetAddress

enum class InformationType{
    Text, Icon, TextIcon
}
data class InformationIPModel(
    val id: Int,
    val title: String,
    var content: String?= null,
    @DrawableRes var icon: Int? = null,
    val type: InformationType = InformationType.Text,
) : KeyModel {
    override val identity: String
        get() = title

    fun updateContent(content: String) = this.copy(content = content)
}

