package com.sangtb.androidlibrary.base.action

import android.util.Log

private const val TAG = "IPermissionCallBack"

interface IPermissionCallBack {

    fun onPermissionGrant(list: Array<String>) {
        Log.d(TAG, "onPermissionGrant: ")
    }

    fun onPermissionBlock(list: Array<String>) {
        Log.d(TAG, "blockedPermissions: ")
    }

    fun onPermissionDenied(list: Array<out String>) {
        Log.d(TAG, "deniedPermissions: ")
    }
}