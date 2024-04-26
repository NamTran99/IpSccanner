package com.victorb.androidnetworkscanner.ui

import android.os.Bundle
import com.victorb.androidnetworkscanner.R
import com.victorb.androidnetworkscanner.core.base.BaseActivity
import com.victorb.androidnetworkscanner.data.local.model.OptionDrawerModel
import com.victorb.androidnetworkscanner.databinding.ActivityMainBinding
import com.victorb.androidnetworkscanner.ui.recycler.DrawerOptionItemAdapter

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private lateinit var optionDrawerAdapter: DrawerOptionItemAdapter

    override val layoutId: Int
        get() = R.layout.activity_main

    fun getDrawer() = binding.drawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        // Default behaviour
        super.onCreate(savedInstanceState)
        binding.apply {
            optionDrawerAdapter = DrawerOptionItemAdapter().apply {
                setListItem(
                    listOf(
                        OptionDrawerModel(
                            R.drawable.ic_icon_app,
                            getString(R.string.option_information),
                            isSelected = true
                        ),
                        OptionDrawerModel(
                            R.drawable.ic_icon_app,
                            getString(R.string.option_scan_network)
                        )
                    )
                )
            }
            rvListOption.adapter = optionDrawerAdapter
        }
    }
}