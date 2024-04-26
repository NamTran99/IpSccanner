package com.victorb.androidnetworkscanner.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.marsounjan.icmp4a.Icmp
import com.marsounjan.icmp4a.Icmp4a
import com.victorb.androidnetworkscanner.Device
import com.victorb.androidnetworkscanner.R
import com.victorb.androidnetworkscanner.core.base.BaseFragment
import com.victorb.androidnetworkscanner.data.local.model.PingStatusLocal
import com.victorb.androidnetworkscanner.data.local.model.convertToPingStatusLocal
import com.victorb.androidnetworkscanner.databinding.FragmentPingDeviceBinding
import com.victorb.androidnetworkscanner.extension.serializableCustom
import com.victorb.androidnetworkscanner.ui.recycler.PingResultAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

class FragmentPingDevices : BaseFragment<FragmentPingDeviceBinding>() {

    companion object {
        private const val ARG_DEVICE_IP = "ARG_DEVICE_IP"
        fun getFragmentPingDevicesBundle(data: Device): Bundle {
            val bundle = Bundle()
            bundle.putSerializable(ARG_DEVICE_IP, data)
            return bundle
        }
    }

    override val layoutId: Int
        get() = R.layout.fragment_ping_device

    private val icmp = Icmp4a()
    private var device: Device? = null
    private lateinit var pingResultNetWorkAdapter: PingResultAdapter


    private fun getData() {
        device = arguments?.serializableCustom(ARG_DEVICE_IP)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()
        initView()
    }

    private fun pingNetWork() {
        var index = 0
        try {
            icmp.pingInterval(
                device?.ip?: "",
                count = 5,
                intervalMillis = 1000
            )
                .onEach { status ->
                    val listItem  = mutableListOf<PingStatusLocal>()
                    Log.d(TAG, "NamTD8 pingNetWork: ${status}")
                    listItem.add(status.convertToPingStatusLocal(index))
                    pingResultNetWorkAdapter.addMoreItem(listItem)
                    index += 1
                    return@onEach

//                    withContext(Dispatchers.Main){
//
//
////                        val result = status.result
////                        when (result) {
////                            is Icmp.PingResult.Success -> Log.d("ICMP", "$host(${status.ip.hostAddress}) ${result.packetSize} bytes - ${result.ms} ms")
////                            is Icmp.PingResult.Failed -> Log.d("ICMP", "$host(${status.ip.hostAddress}) Failed: ${result.message}")
////                        }
//                    }
                }
                .launchIn(lifecycleScope)
        } catch (error: Icmp.Error.UnknownHost) {
            Log.d("ICMP", "Unknown host $host")
        }
    }

    private fun initView() {
        binding.apply {
            tvName.text = device?.hostname
            tvIp.text = device?.ip

            pingResultNetWorkAdapter = PingResultAdapter().apply {
                setHostData(device)
            }

            rvListPing.adapter = pingResultNetWorkAdapter

            customToolbar.onScanClicked = {
                pingNetWork()
            }
        }
    }
}


