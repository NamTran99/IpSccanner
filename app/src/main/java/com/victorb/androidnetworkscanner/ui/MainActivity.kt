package com.victorb.androidnetworkscanner.ui

import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.sangtb.androidlibrary.base.action.IPermissionCallBack
import com.victorb.androidnetworkscanner.R
import com.victorb.androidnetworkscanner.core.base.BaseActivity
import com.victorb.androidnetworkscanner.core.permission.AppPermission
import com.victorb.androidnetworkscanner.core.permission.IAppPermission
import com.victorb.androidnetworkscanner.data.local.model.OptionDrawerModel
import com.victorb.androidnetworkscanner.databinding.ActivityMainBinding
import com.victorb.androidnetworkscanner.ui.recycler.DrawerOptionItemAdapter

class MainActivity : BaseActivity<ActivityMainBinding>(), IAppPermission by AppPermission()  {
    private lateinit var optionDrawerAdapter: DrawerOptionItemAdapter

    override val layoutId: Int
        get() = R.layout.activity_main

    fun getDrawer() = binding.drawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        // Default behaviour
        super.onCreate(savedInstanceState)
        registerActivityResult(::registerForActivityResult)

        binding.apply {
            optionDrawerAdapter = DrawerOptionItemAdapter().apply {
                onItemClick = {
                    navigateToDestination(
                        it.idNavigation,
                        inclusive = true,
                        popUpToDes =  binding.navHostFragmentContainer.findNavController().currentDestination?.id

                    )
                }
                setListItem(
                    listOf(
                        OptionDrawerModel(
                            R.drawable.ic_icon_app,
                            getString(R.string.option_information),
                            isSelected = true,
                            idNavigation = R.id.fragmentIPInformationDetail
                        ),
                        OptionDrawerModel(
                            R.drawable.ic_icon_app,
                            getString(R.string.option_scan_network),
                            idNavigation = R.id.fragmentSearchIP
                        )
                    )
                )
            }
            rvListOption.adapter = optionDrawerAdapter
        }
    }


    open fun navigateToDestination(
        destination: Int,
        bundle: Bundle? = null,
        inclusive: Boolean = false,
        popUpToDes: Int? = null,
        navOption: NavOptions.Builder? = null
    ) {
        val navOptionBuilder = navOption ?: NavOptions.Builder()

        popUpToDes?.let {
            navOptionBuilder
                .setPopUpTo(destinationId = it, inclusive = inclusive)
        }

        binding.navHostFragmentContainer.findNavController().apply {
            navigate(destination, bundle, navOptions = navOptionBuilder.build())
        }
    }
}