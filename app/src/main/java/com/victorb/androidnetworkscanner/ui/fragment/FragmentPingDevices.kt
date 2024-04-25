package com.victorb.androidnetworkscanner.ui.fragment

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.marsounjan.icmp4a.Icmp
import com.marsounjan.icmp4a.Icmp4a
import com.victorb.androidnetworkscanner.Device
import com.victorb.androidnetworkscanner.R
import com.victorb.androidnetworkscanner.ResultsAdapter
import com.victorb.androidnetworkscanner.core.base.BaseFragment
import com.victorb.androidnetworkscanner.databinding.FragmentIpScannerBinding
import com.victorb.androidnetworkscanner.databinding.FragmentPingDeviceBinding
import com.victorb.androidnetworkscanner.extension.hide
import com.victorb.androidnetworkscanner.extension.show
import com.victorb.androidnetworkscanner.generateIpRange
import com.victorb.androidnetworkscanner.getIpHostname
import com.victorb.androidnetworkscanner.getNetworkPrefixLength
import com.victorb.androidnetworkscanner.getPhoneIp
import com.victorb.androidnetworkscanner.intIpToReversedIntIp
import com.victorb.androidnetworkscanner.intIpToString
import com.victorb.androidnetworkscanner.isIpReachable
import com.victorb.androidnetworkscanner.isWifiConnected
import com.victorb.androidnetworkscanner.isWifiEnabled
import com.victorb.androidnetworkscanner.runOnMainThread
import com.victorb.androidnetworkscanner.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentPingDevices : BaseFragment<FragmentPingDeviceBinding>() {

    companion object{
        private const val ARG_DEVICE_IP = "ARG_DEVICE_IP"
        fun getFragmentPingDevicesBundle(data: Device): Bundle {
            val bundle = Bundle()
            bundle.putParcelable(ARG_DEVICE_IP, data)
            return bundle
        }
    }

    override val layoutId: Int
        get() = R.layout.fragment_ping_device

    private val icmp = Icmp4a()
    val device :Device? = null

    fun getData(){
        device = arguments.getParcelable(ARG_DEVICE_IP, Device::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            val host = "google.com"
            try {
                val status = icmp.ping( host = "192.168.137.252")
                when (val result = status.result) {
                    is Icmp.PingResult.Success -> Log.d("ICMP", "$host(${status.ip.hostAddress}) ${result.packetSize} bytes - ${result.ms} ms")
                    is Icmp.PingResult.Failed -> Log.d("ICMP", "$host(${status.ip.hostAddress}) Failed: ${result.message}")
                }
            } catch (error: Icmp.Error.UnknownHost) {
                Log.d("ICMP", "Unknown host $host")
            }
        }
    }

}


