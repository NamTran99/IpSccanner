package com.victorb.androidnetworkscanner.core.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.victorb.androidnetworkscanner.ui.MainActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {
    val TAG by lazy { this::class.java.name }

    fun getMainActivity() = activity as? MainActivity

    open lateinit var binding: T
    open val isSaveCache = false

    protected var jopEventReceiver: Job? = null

    @get:LayoutRes
    abstract val layoutId: Int

    override fun onAttach(ctx: Context) {
        super.onAttach(ctx)
        Log.d(TAG, "onAttach: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: NamTD8")
        return if (isSaveCache) {
            return if (::binding.isInitialized) {
                binding.lifecycleOwner = viewLifecycleOwner
                binding.root
            } else {
                binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
                binding.apply {
                    lifecycleOwner = viewLifecycleOwner
                }.root
            }
        } else {
            binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
            binding.apply {
                lifecycleOwner = viewLifecycleOwner
            }.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: $savedInstanceState")

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackFragment()
                }
            })
    }

    open fun onResponse(key: String, data: Any?) {
        Log.d(TAG, "onResponse: Called key: $key ---- data: $data")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        onReload()
        Log.d(TAG, "onResume: ")
    }

    open fun onReload() {
        Log.d(TAG, "onReload: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    open fun showToast(content: String, duration: Long = 2000) {
        Log.d(TAG, "showToast: content: $content ---- type: $duration")
        Toast.makeText(requireContext(), content, Toast.LENGTH_SHORT).show()
    }

    open fun showToast(res: Int, duration: Long = 2000) {
        Log.d(TAG, "showToast: content: $res ---- type: $duration")
        Toast.makeText(requireContext(), getString(res), Toast.LENGTH_SHORT).show()
    }


    open fun onBackFragment() {
        Log.d(TAG, "onBackFragment: ")
        val navHostFragment = this.parentFragment as? NavHostFragment
        if (navHostFragment != null && navHostFragment.childFragmentManager.backStackEntryCount == 0) {
//            baseActivity?.backTwoTimeCloseApp()
        } else {
            (binding.root as? ViewGroup)?.removeAllViews()
            findNavController().popBackStack()
        }
    }

    open fun navigateToDestination(destination: Int, bundle: Bundle? = null) {
        Log.d(TAG, "navigateToDestination: ")
        findNavController().apply {
            bundle?.let {
                navigate(destination, it)
            } ?: navigate(destination)
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

        findNavController().apply {
            navigate(destination, bundle, navOptions = navOptionBuilder.build())
        }
    }

    open fun openAnotherApp(packageName: String, bundle: Bundle?) {
        val launch = context?.packageManager?.getLaunchIntentForPackage(packageName)
        launch?.let {
            it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(it, bundle)
        }
    }

    open fun closeApp() {
        activity?.finishAffinity()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView: ")
        jopEventReceiver?.cancel()
        super.onDestroyView()
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach: ")
        super.onDetach()
    }

    fun onClearViewModelInScopeActivity() {
        activity?.viewModelStore?.clear()
    }
}