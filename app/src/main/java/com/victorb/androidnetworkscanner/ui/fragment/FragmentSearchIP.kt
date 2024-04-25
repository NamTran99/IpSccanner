package com.victorb.androidnetworkscanner.ui.fragment

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.marsounjan.icmp4a.Icmp
import com.marsounjan.icmp4a.Icmp4a
import com.victorb.androidnetworkscanner.R
import com.victorb.androidnetworkscanner.ResultsAdapter
import com.victorb.androidnetworkscanner.core.base.BaseFragment
import com.victorb.androidnetworkscanner.databinding.FragmentIpScannerBinding
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentSearchIP : BaseFragment<FragmentIpScannerBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_ip_scanner

    // UI
    private var animator: ObjectAnimator? = null
    private var resultsAdapter = ResultsAdapter()

    // Coroutines
    private val scanJobScope = CoroutineScope(Dispatchers.Default)
    private var currentScanJob: Job? = null
    private val checkJobsScope = CoroutineScope(Dispatchers.IO)

    val icmp = Icmp4a()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            // Setup the toolbar

            lifecycleScope.launch {
                val host = "google.com"
                try {
                    val status = icmp.ping(host = "192.168.137.252")
                    when (val result = status.result) {
                        is Icmp.PingResult.Success -> Log.d(
                            "ICMP",
                            "$host(${status.ip.hostAddress}) ${result.packetSize} bytes - ${result.ms} ms"
                        )

                        is Icmp.PingResult.Failed -> Log.d(
                            "ICMP",
                            "$host(${status.ip.hostAddress}) Failed: ${result.message}"
                        )
                    }
                } catch (error: Icmp.Error.UnknownHost) {
                    Log.d("ICMP", "Unknown host $host")
                }
            }


            customToolbar.onStartIconClicked = {
                getMainActivity()?.getDrawer()?.open()
            }

            // Set the LayoutManager and the Adapter for RecyclerView
            rvIps.adapter = resultsAdapter.apply {
                onSizeChange = {
                    layoutNoData.show(it == 0)
                    binding.rvIps.show(it != 0)
                    binding.customToolbar.title = getString(R.string.local_network, it)
                }

                onItemClick = {
                    navigateToDestination(
                        R.id.action_fragmentSearchIP_to_fragmentPingDevices,
                        FragmentPingDevices.getFragmentPingDevicesBundle(it)
                    )
                }
            }

            binding.customToolbar.onScanClicked = {
                binding.customToolbar.title = getString(R.string.local_network_default)
                resultsAdapter.clear()
                startScan(requireContext())
            }

            // Start the scan
            currentScanJob = startScan(requireContext())
        }
    }

    var progress = 0
        set(value) {
            val percentage = value * 100 / 256
            binding.progressBar.progress = percentage
            binding.progressBar.hide(value == 256)
            field = value
        }

    private fun startScan(context: Context): Job =
        scanJobScope.launch {
            withContext(Dispatchers.Main) {
                progress = 0
            }

            // Check if wifi is on
            if (isWifiEnabled(context) && isWifiConnected(context)) {
                // Start the refresh button animation
                runOnMainThread { animator?.start() }
                // List of check jobs
                val checkingJobs: ArrayList<Job> = arrayListOf()

                // Iterate through all the possible IPs
                for (ip in generateIpRange(
                    intIpToReversedIntIp(getPhoneIp(context)),
                    getNetworkPrefixLength(context)
                )) {
                    // Add the jobs, which checks if the connection is up and adds it to the adapter
                    checkingJobs.add(checkJobsScope.launch {
                        val reversedIp: Int = intIpToReversedIntIp(ip)
                        if (isIpReachable(reversedIp)) {
                            val hostname: String = getIpHostname(reversedIp)
                            val ipString: String = intIpToString(reversedIp)
                            resultsAdapter.addItem(ipString, hostname)
                        }
                        withContext(Dispatchers.Main) {
                            progress += 1
                        }
                    }
                    )
                }
                // Wait for the checking jobs to finish
                checkingJobs.joinAll()
            } else {
                runOnMainThread {
                    Toast.makeText(
                        context,
                        "Please enable Wifi and connect to an access point",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
}


