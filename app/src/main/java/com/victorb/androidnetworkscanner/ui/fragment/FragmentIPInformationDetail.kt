package com.victorb.androidnetworkscanner.ui.fragment

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.Network
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.blongho.country_data.World
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sangtb.androidlibrary.base.action.IPermissionCallBack
import com.victorb.androidnetworkscanner.R
import com.victorb.androidnetworkscanner.core.permission.AppPermission
import com.victorb.androidnetworkscanner.core.permission.IAppPermission
import com.victorb.androidnetworkscanner.data.local.model.InformationIPModel
import com.victorb.androidnetworkscanner.data.local.model.InformationType
import com.victorb.androidnetworkscanner.databinding.FragmentInformationMyDeviceBinding
import com.victorb.androidnetworkscanner.extension.LocationManager
import com.victorb.androidnetworkscanner.extension.SystemManager
import com.victorb.androidnetworkscanner.getBroadcastAddress
import com.victorb.androidnetworkscanner.getIpHostname
import com.victorb.androidnetworkscanner.getPhoneIpv4
import com.victorb.androidnetworkscanner.getWifiBroadcast
import com.victorb.androidnetworkscanner.intIpToString
import com.victorb.androidnetworkscanner.ui.MainActivity
import com.victorb.androidnetworkscanner.ui.recycler.ItemInformationAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.TimeZone

class FragmentIPInformationDetail : Fragment(), IAppPermission by AppPermission.getInstance() {
    companion object {
        const val LO_WIFI_STATE = 0
        const val LO_COUNTRY = 1
        const val LO_COUNTRY_CODE = 2
        const val LO_CONTINENT = 3
        const val LO_CITY = 4
        const val LO_LAT = 5
        const val LO_LONG = 6

        const val WI_SSID = 0
        const val WI_BSSID = 1
        const val WI_GATEWAY = 2
        const val WI_BROADCAST = 3
        const val WI_SUBNET_MASK = 4
        const val WI_IPV4 = 5
    }


    lateinit var binding: FragmentInformationMyDeviceBinding

    private fun getMainActivity() = activity as? MainActivity

    private lateinit var itemInforLocationAdapter: ItemInformationAdapter
    private lateinit var itemInforWifiDetailAdapter: ItemInformationAdapter


    val listLocation by lazy {
        mutableListOf(
            InformationIPModel(
                id = LO_WIFI_STATE,
                getString(R.string.title_infor_wifi_state),
                icon = R.drawable.ic_dot_disconnected,
                type = InformationType.Icon
            ),
            InformationIPModel(
                id = LO_COUNTRY,
                getString(R.string.title_infor_country),
                type = InformationType.TextIcon
            ),
            InformationIPModel(
                id = LO_COUNTRY_CODE,
                getString(R.string.title_infor_country_code)
            ),
            InformationIPModel(id = LO_CONTINENT, getString(R.string.title_infor_continent)),
            InformationIPModel(id = LO_CITY, getString(R.string.title_infor_city)),
            InformationIPModel(id = LO_LAT, getString(R.string.title_infor_latitude)),
            InformationIPModel(id = LO_LONG, getString(R.string.title_infor_longitude)),
        )
    }

    val listWifiDetail by lazy {
        mutableListOf(
            InformationIPModel(id = WI_SSID, getString(R.string.title_infor_ssid)),
            InformationIPModel(id = WI_BSSID, getString(R.string.title_infor_bssid)),
            InformationIPModel(
                id = WI_GATEWAY,
                getString(R.string.title_infor_default_gateway_ip)
            ),
            InformationIPModel(
                id = WI_BROADCAST,
                getString(R.string.title_infor_wifi_broadcast)
            ),
            InformationIPModel(id = WI_SUBNET_MASK, getString(R.string.title_infor_subnet_mask)),
            InformationIPModel(id = WI_IPV4, getString(R.string.title_infor_ipv4)),
        )
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentInformationMyDeviceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            // Setup the toolbar
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            World.init(requireContext())


            customToolbar.onStartIconClicked = {
                getMainActivity()?.getDrawer()?.open()
            }

            initView()

            registerActivityResult(::registerForActivityResult)

            requestPermissions(
                requireContext(),
                arrayOf(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION"
                ),
                callBack = object : IPermissionCallBack {
                    @SuppressLint("MissingPermission")
                    override fun onPermissionGrant(list: Array<String>) {
                        fusedLocationClient.getLastLocation().addOnSuccessListener {
                            listLocation[LO_LAT] =
                                listLocation[LO_LAT].updateContent(it.latitude.toString())
                            listLocation[LO_LONG] =
                                listLocation[LO_LONG].updateContent(it.longitude.toString())
                            val address = LocationManager.getAddress(it.latitude, it.longitude)
                            val city = address?.locality ?: address?.adminArea
                            listLocation[LO_CITY].content = city
                            updateListLocation()
                        }
                    }
                })
        }
    }


    private fun initView() {
        itemInforLocationAdapter = ItemInformationAdapter()
        itemInforWifiDetailAdapter = ItemInformationAdapter()

        binding.apply {
            rvLocation.adapter = itemInforLocationAdapter
            rvWifiDetails.adapter = itemInforWifiDetailAdapter
        }
        updateListLocation()
        updateListWifiState()

        getPhoneIP()

        SystemManager.registerCheckConnectivityStatus(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                getDataWhenWifiAvailable()
                super.onAvailable(network)
            }

            override fun onLost(network: Network) {
                itemInforLocationAdapter.updateItem(
                    InformationIPModel(
                        id = LO_WIFI_STATE,
                        getString(R.string.title_infor_wifi_state),
                        icon = R.drawable.ic_dot_disconnected,
                        type = InformationType.Icon
                    ),
                )
                super.onLost(network)
            }
        })
    }

    private fun getDataWhenWifiAvailable() {
        getPhoneIP()

        getLocationDetail()
        getWifiInfoDetail()
    }

    private fun getLocationDetail() {
        val countryCode = SystemManager.getCountryCode()
        val country = World.getCountryFrom(countryCode)
        val flag = World.getFlagOf(countryCode)
        listLocation[LO_WIFI_STATE].icon = R.drawable.ic_dot_alive
        listLocation[LO_COUNTRY_CODE].content = countryCode
        listLocation[LO_COUNTRY].apply {
            content = country.name
            icon = flag
        }
        listLocation[LO_CONTINENT].content = country.continent
        updateListLocation()
    }

    private fun updateListLocation() {
        itemInforLocationAdapter.submitList(listLocation)
    }

    private fun updateListWifiState() {
        itemInforWifiDetailAdapter.submitList(listWifiDetail)
    }

    private fun getWifiInfoDetail() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val wifiInfo = SystemManager.getWifiInfo()
                val dhcpInfo = requireContext().getSystemService(WifiManager::class.java).dhcpInfo
                wifiInfo?.let {
                    listWifiDetail[WI_SSID].content = it.ssid
                    listWifiDetail[WI_BSSID].content = it.bssid
                    listWifiDetail[WI_IPV4].content = intIpToString(dhcpInfo.ipAddress)
                    listWifiDetail[WI_GATEWAY].content = intIpToString(dhcpInfo.gateway)
                    listWifiDetail[WI_BROADCAST].content = getWifiBroadcast(dhcpInfo.ipAddress)
                    listWifiDetail[WI_SUBNET_MASK].content = "255.255.255.0"
                    updateListWifiState()
                }
            }
        }

    }

    private fun getPhoneIP() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                binding.tvIpName.text = getIpHostname(getPhoneIpv4(requireContext()))
            }
        }

    }

}


