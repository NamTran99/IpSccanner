package com.victorb.androidnetworkscanner.ui.fragment

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.victorb.androidnetworkscanner.ResultsAdapter
import com.victorb.androidnetworkscanner.databinding.FragmentIpScannerBinding
import com.victorb.androidnetworkscanner.extension.hide
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
import com.victorb.androidnetworkscanner.runOnMainThreadDelayed
import com.victorb.androidnetworkscanner.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentMyIPDetail : Fragment() {

    lateinit var binding: FragmentIpScannerBinding

    private fun getMainActivity() = activity as? MainActivity

    // UI
    private var animator: ObjectAnimator? = null
    private var resultsAdapter = ResultsAdapter()

    // Coroutines
    private val scanJobScope = CoroutineScope(Dispatchers.Default)
    private var currentScanJob: Job? = null
    private val checkJobsScope = CoroutineScope(Dispatchers.IO)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentIpScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            // Setup the toolbar

            customToolbar.onStartIconClicked = {
                getMainActivity()?.getDrawer()?.open()
            }

            // Set the LayoutManager and the Adapter for RecyclerView
            rvIps.adapter = resultsAdapter

            // Start a scan
            // The delay is necessary to find the toolbar refresh button
            // See : https://stackoverflow.com/questions/28840815/menu-item-animation-rotate-indefinitely-its-custom-icon
            runOnMainThreadDelayed(100) {

                binding.customToolbar.onScanClicked = {
                    startScan(requireContext())
                }

                // Start the scan
                currentScanJob = startScan(requireContext())
            }
        }
    }

    var progress = 0
        set(value) {
            val percentage = value * 100 / 256
            binding.progressBar.progress = percentage
            binding.progressBar.hide(value == 256)
            field = value
        }

    private fun startScan(context: Context): Job = scanJobScope.launch {
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
                intIpToReversedIntIp(getPhoneIp(context)), getNetworkPrefixLength(context)
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
                })
            }
            // Wait for the checking jobs to finish
            checkingJobs.joinAll()
            // Stop the animation
            runOnMainThread { animator?.cancel() }
            // Wifi is off
        } else {
            runOnMainThread {
                Toast.makeText(
                    context, "Please enable Wifi and connect to an access point", Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}


