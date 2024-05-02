package com.victorb.androidnetworkscanner.ui.fragment

import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.victorb.androidnetworkscanner.R
import com.victorb.androidnetworkscanner.data.local.model.InformationIPModel
import com.victorb.androidnetworkscanner.data.local.model.InformationType
import com.victorb.androidnetworkscanner.databinding.FragmentInformationMyDeviceBinding
import com.victorb.androidnetworkscanner.getIpHostname
import com.victorb.androidnetworkscanner.getPhoneIp
import com.victorb.androidnetworkscanner.ui.MainActivity
import com.victorb.androidnetworkscanner.ui.recycler.ItemInformationAdapter

class FragmentIPInformationDetail : Fragment() {
    companion object {
        const val LO_WIFI_STATE = 0
        const val LO_COUNTRY = 1
        const val LO_CONTINENT = 2
        const val LO_COUNTRY_CODE = 3
        const val LO_CITY = 4
        const val LO_LAT = 5
        const val LO_LONG = 6
        const val LO_IPS = 7
        const val LO_TIME_ZONE = 8
        const val LO_REGION_NAME = 9


        const val WI_PROXY = 0
        const val WI_BSSID = 1
        const val WI_BROADCAST = 2
        const val WI_GATEWAY = 3
        const val WI_IPV4 = 4
        const val WI_IPV6 = 5
    }


    lateinit var binding: FragmentInformationMyDeviceBinding

    private fun getMainActivity() = activity as? MainActivity

    private lateinit var itemInforLocationAdapter: ItemInformationAdapter
    private lateinit var itemInforWifiDetailAdapter: ItemInformationAdapter



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

            customToolbar.onStartIconClicked = {
                getMainActivity()?.getDrawer()?.open()
            }

            initView()
        }
    }

    private fun initView() {
        itemInforLocationAdapter = ItemInformationAdapter()
        itemInforWifiDetailAdapter = ItemInformationAdapter()

        binding.apply {
            rvLocation.adapter = itemInforLocationAdapter
            rvWifiDetails.adapter = itemInforWifiDetailAdapter
        }


        itemInforLocationAdapter.submitList(
            listOf(
                InformationIPModel(
                    id = LO_WIFI_STATE,
                    getString(R.string.title_infor_wifi_state),
                    icon = R.drawable.ic_dot_disconnected,
                    type = InformationType.Icon
                ),
                InformationIPModel(id = LO_COUNTRY, getString(R.string.title_infor_country)),
                InformationIPModel(id = LO_CONTINENT, getString(R.string.title_infor_continent)),
                InformationIPModel(
                    id = LO_COUNTRY_CODE,
                    getString(R.string.title_infor_country_code)
                ),
                InformationIPModel(
                    id = LO_REGION_NAME,
                    getString(R.string.title_infor_region_name)
                ),
                InformationIPModel(id = LO_CITY, getString(R.string.title_infor_city)),
                InformationIPModel(id = LO_LAT, getString(R.string.title_infor_latitude)),
                InformationIPModel(id = LO_LONG, getString(R.string.title_infor_longitude)),
                InformationIPModel(id = LO_IPS, getString(R.string.title_infor_isp)),
                InformationIPModel(id = LO_TIME_ZONE, getString(R.string.title_infor_timezone)),
            )
        )

        itemInforWifiDetailAdapter.submitList(
            listOf(
                InformationIPModel(id = WI_PROXY, getString(R.string.title_infor_proxy)),
                InformationIPModel(id = WI_BSSID, getString(R.string.title_infor_bssid)),
                InformationIPModel(
                    id = WI_BROADCAST,
                    getString(R.string.title_infor_wifi_broadcast)
                ),
                InformationIPModel(
                    id = WI_GATEWAY,
                    getString(R.string.title_infor_default_gateway_ip)
                ),
                InformationIPModel(id = WI_IPV4, getString(R.string.title_infor_subnet_mask)),
                InformationIPModel(id = WI_IPV6, getString(R.string.title_infor_ipv4)),
            )
        )

        getPhoneIP()


        val connectivityManager = requireContext().getSystemService(ConnectivityManager::class.java)

        connectivityManager.registerDefaultNetworkCallback(object :
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

    private fun getDataWhenWifiAvailable(){
        getPhoneIP()

        getLocationDetail()
        getWifiInfoDetail()
    }

    private fun getLocationDetail() {
        itemInforLocationAdapter.updateItem(
            InformationIPModel(
                id = LO_WIFI_STATE,
                getString(R.string.title_infor_wifi_state),
                icon = R.drawable.ic_dot_alive,
                type = InformationType.Icon
            ),
        )
    }

    private fun getWifiInfoDetail() {

    }

    private fun getPhoneIP() {
        binding.tvIpName.text = getIpHostname(getPhoneIp(requireContext()))
    }

}


