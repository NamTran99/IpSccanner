package com.victorb.androidnetworkscanner.ui.fragment

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.animation.doOnCancel
import androidx.fragment.app.Fragment
import com.victorb.androidnetworkscanner.ui.MainActivity
import com.victorb.androidnetworkscanner.ResultsAdapter
import com.victorb.androidnetworkscanner.databinding.FragmentIpScannerBinding
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class FragmentSearchIP : Fragment() {

    lateinit var binding: FragmentIpScannerBinding

    fun getMainActivity() = activity as? MainActivity

    // UI
    private var animator: ObjectAnimator? = null
    private var resultsAdapter = ResultsAdapter()

    // Coroutines
    private val scanJobScope = CoroutineScope(Dispatchers.Default)
    private var currentScanJob: Job? = null
    private val checkJobsScope = CoroutineScope(Dispatchers.IO)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIpScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            // Setup the toolbar
//            setSupportActionBar(binding.toolbar)

            customToolbar.onStartIconClicked = {
                getMainActivity()?.getDrawer()?.open()
            }

            // Set the LayoutManager and the Adapter for RecyclerView
            rvIps.adapter = resultsAdapter

            // Start a scan
            // The delay is necessary to find the toolbar refresh button
            // See : https://stackoverflow.com/questions/28840815/menu-item-animation-rotate-indefinitely-its-custom-icon
            runOnMainThreadDelayed(100) {
                // Create the animator
//                val view = findViewById<View>(R.id.action_refresh)
                animator = ObjectAnimator.ofFloat(view, "rotation", 360f).apply {
                    duration = 1000
                    repeatCount = Animation.INFINITE
                    interpolator = LinearInterpolator()
                    doOnCancel {
//                        view.rotation = 0f
                    }
                }

                // Start the scan
//                currentScanJob = startScan(requireContext())
            }
        }
    }

//    private fun startScan(context: Context): Job =
//        scanJobScope.launch {
//            // Check if wifi is on
//            if (isWifiEnabled(context) && isWifiConnected(context)) {
//                // Start the refresh button animation
//                runOnMainThread { animator?.start() }
//                // List of check jobs
//                val checkingJobs: ArrayList<Job> = arrayListOf()
//                // Iterate through all the possible IPs
//                for (ip in generateIpRange(
//                    intIpToReversedIntIp(getPhoneIp(context)),
//                    getNetworkPrefixLength(context)
//                )) {
//                    // Add the jobs, which checks if the connection is up and adds it to the adapter
//                    checkingJobs.add(checkJobsScope.launch {
//                        val reversedIp: Int = intIpToReversedIntIp(ip)
//                        if (isIpReachable(reversedIp)) {
//                            val hostname: String = getIpHostname(reversedIp)
//                            val ipString: String = intIpToString(reversedIp)
//                            resultsAdapter.addItem(ipString, hostname)
//                        }
//                    })
//                }
//                // Wait for the checking jobs to finish
//                checkingJobs.joinAll()
//                // Stop the animation
//                runOnMainThread { animator?.cancel() }
//                // Wifi is off
//            } else {
//                runOnMainThread {
//                    Toast.makeText(
//                        context,
//                        "Please enable Wifi and connect to an access point",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
//        }
}