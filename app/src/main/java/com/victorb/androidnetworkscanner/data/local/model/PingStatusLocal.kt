package com.victorb.androidnetworkscanner.data.local.model

import com.marsounjan.icmp4a.Icmp
import com.victorb.androidnetworkscanner.core.base.recyclerview.KeyModel
import java.net.InetAddress

data class PingStatusLocal(
    val id: Int,
    val ip: InetAddress,
    val packetsTransmitted: Int,
    val packetsReceived: Int,
    val packetLoss: Float,
    val stats: Icmp.LatencyStats?,
    val result: Icmp.PingResult
) : KeyModel {
    override val identity: String
        get() = id.toString()
}

fun Icmp.PingStatus.convertToPingStatusLocal(index: Int) =
    PingStatusLocal(
        id = index,
        ip = this.ip,
        packetsTransmitted = this.packetsTransmitted,
        packetsReceived = this.packetsReceived,
        packetLoss = this.packetLoss,
        stats = this.stats,
        result = this.result
    )
