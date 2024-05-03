package com.victorb.androidnetworkscanner.core.permission

import android.app.Activity
import android.content.Context
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.sangtb.androidlibrary.base.action.IPermissionCallBack
import com.victorb.androidnetworkscanner.extension.SingletonHolder

/**
 * @property callbackApp have 3 method {onPermissionGrant,onPermissionBlock,onPermissionDenied} => default set value = null
 * @property launcherPermission launcherPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(),this)
 *
 * */
interface IAppPermission : ActivityResultCallback<Map<String, Boolean>>, IPermissionCallBack {
    var callbackApp: IPermissionCallBack
    var launcherPermission: ActivityResultLauncher<Array<String>>?
    var context : Any?

    override fun onActivityResult(result: Map<String, Boolean>) {
        val listPermissionGrant = arrayListOf<String>()
        val listPermissionNotGranted = arrayListOf<String>()
        val listPermissionDenied = arrayListOf<String>()

        result.entries.forEach {
            val listTmp =
                if (it.value) listPermissionGrant else if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        (context as? Fragment)?.requireActivity()
                            ?: context as Activity, it.key
                    )
                ) listPermissionNotGranted else listPermissionDenied
            listTmp.add(it.key)
        }

        if (listPermissionGrant.size > 0) {
            callbackApp.onPermissionGrant(listPermissionGrant.toTypedArray())
        }

        if (listPermissionDenied.size > 0) {
            callbackApp.onPermissionDenied(listPermissionDenied.toTypedArray())
            return
        }

        if (listPermissionNotGranted.size > 0) {
            callbackApp.onPermissionBlock(listPermissionNotGranted.toTypedArray())
            return
        }
    }

    fun registerActivityResult(callback: (ActivityResultContract<Array<String>, Map<String, Boolean>>, ActivityResultCallback<Map<String, Boolean>>) -> ActivityResultLauncher<Array<String>>) {
        launcherPermission = callback.invoke(
            ActivityResultContracts.RequestMultiplePermissions(), ::onActivityResult
        )
    }

    fun requestPermission(
        context: Context,
        callBack: IPermissionCallBack = this,
        @NonNull vararg permission: String,
    ) {
        this.context = context
        this.callbackApp = callBack
        val tmp = arrayListOf<String>()
        permission.forEach { tmp.add(it) }
        launcherPermission?.launch(tmp.toTypedArray())
    }

    fun requestPermissions(
        context: Context,
        @NonNull permissions: Array<String>,
        callBack: IPermissionCallBack = this
    ) {
        this.context = context
        this.callbackApp = callBack
        launcherPermission?.launch(permissions)
    }
}

class AppPermission : IAppPermission {
    override var context: Any? = null
    override var callbackApp: IPermissionCallBack = this
    override var launcherPermission: ActivityResultLauncher<Array<String>>? = null

    companion object : SingletonHolder<AppPermission>(::AppPermission)
}