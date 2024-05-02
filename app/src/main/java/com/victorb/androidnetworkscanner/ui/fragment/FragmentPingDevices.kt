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
import com.victorb.androidnetworkscanner.extension.hide
import com.victorb.androidnetworkscanner.extension.moveViewFromBottom
import com.victorb.androidnetworkscanner.extension.serializableCustom
import com.victorb.androidnetworkscanner.extension.show
import com.victorb.androidnetworkscanner.ui.recycler.PingResultAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
                device?.ip ?: "",
                count = 5,
                intervalMillis = 1000
            )
                .onEach { status ->
                    val listItem = mutableListOf<PingStatusLocal>()

                    listItem.add(status.convertToPingStatusLocal(index))

                    pingResultNetWorkAdapter.addMoreItem(listItem) {
                        if (index % 5 == 4) {
                            val listResult = it
                            val numReceived = listResult.count {
                                it.result is Icmp.PingResult.Success
                            }

                            val minRes = listResult.filter {
                                it.result is Icmp.PingResult.Success
                            }.minOf {
                                (it.result as? Icmp.PingResult.Success)?.ms ?: 0
                            }

                            val maxRes = listResult.filter {
                                it.result is Icmp.PingResult.Success
                            }.maxOf {
                                (it.result as? Icmp.PingResult.Success)?.ms ?: 0
                            }

                            binding.apply {
                                lvPingStatistic.show()
                                lvPingStatistic.moveViewFromBottom()
                                lvPingStatistic.show {
                                    tvStatistic1.text =
                                        getString(
                                            R.string.ping_statistics_content_1,
                                            numReceived.toString(),
                                            (5 - numReceived).toString(),
                                            ((5 - numReceived) / 5.0).toString()
                                        )

                                    tvStatistic2.text = getString(
                                        R.string.ping_statistics_content_2,
                                        minRes.toString(), maxRes.toString()
                                    )
                                }
                            }
                        }
                    }
                    index += 1
                    Log.d(TAG, "pingNetWork: $index ")
                    return@onEach
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

            pingResultNetWorkAdapter = PingResultAdapter()
            pingResultNetWorkAdapter.setHostData(device)

            rvListPing.adapter = pingResultNetWorkAdapter

            customToolbar.onScanClicked = {
                lvPingStatistic.hide()
                pingResultNetWorkAdapter.clearData()
                pingNetWork()
            }

            customToolbar.onStartIconClicked = {
                getMainActivity()?.getDrawer()?.open()
            }
        }
    }
}


