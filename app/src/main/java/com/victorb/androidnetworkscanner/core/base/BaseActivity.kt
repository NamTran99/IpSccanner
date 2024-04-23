package com.victorb.androidnetworkscanner.core.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

abstract class BaseActivity<VB : ViewDataBinding>():  AppCompatActivity(){

    protected abstract val layoutId: Int
    lateinit var binding: VB
        private set
    open val fragmentContainerView: Int? = null
    lateinit var navHostFragment: NavHostFragment
        private set
    var navController: NavController? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutId)

        if (fragmentContainerView != null) {
            navHostFragment = supportFragmentManager.findFragmentById(fragmentContainerView!!) as NavHostFragment
            navController = navHostFragment.navController
        }
    }
}