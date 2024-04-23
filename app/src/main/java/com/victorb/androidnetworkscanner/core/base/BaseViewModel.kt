package com.victorb.androidnetworkscanner.core.base

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel(application: Application) : AndroidViewModel(application){
    protected val TAG by lazy { this::class.java.name }
    protected val evenSender = Channel<AppEvent>()

    val eventReceiver = evenSender.receiveAsFlow().conflate()
    var checkReLoad: Boolean = false

    open fun onClickClose() {
        viewModelScope.launch {
            evenSender.send(AppEvent.OnCloseApp)
        }
    }

    open fun onBackStack() {
        viewModelScope.launch {
            evenSender.send(AppEvent.OnBackScreen)
        }
    }


    open fun navigateToDestination(
        action: Int, bundle: Bundle? = null, inclusive: Boolean = false, popUpToDes: Int? = null
    ) = viewModelScope.launch {
        evenSender.send(
            AppEvent.OnNavigation1(action, bundle, popUpToDes, inclusive)
        )
    }

    open fun navigateToDestination(action: Int, bundle: Bundle? = null) = viewModelScope.launch {
        evenSender.send(
            AppEvent.OnNavigation(action, bundle)
        )
    }

    open fun backScreen() = viewModelScope.launch {
        evenSender.send(
            AppEvent.OnBackScreen
        )
    }

    open fun closeApp() = viewModelScope.launch {
        evenSender.send(
            AppEvent.OnCloseApp
        )
    }

    open fun showToast(content: String) = viewModelScope.launch {
        evenSender.send(
            AppEvent.OnShowToast(content)
        )
    }

//    open fun showToast(contentID: Int) = viewModelScope.launch {
//        evenSender.send(
//            AppEvent.OnShowToast(getString(contentID))
//        )
//    }

    open fun <T>sendResponse(key: String, data: T) = viewModelScope.launch{
        evenSender.send(
            AppEvent.OnResponse(key,data)
        )
    }
}

sealed class AppEvent {
    class OnNavigation(val destination: Int, val bundle: Bundle? = null) : AppEvent()

    class OnNavigation1(
        val destination: Int,
        val bundle: Bundle? = null,
        val popUpTo: Int? = null,
        val isInclusive: Boolean = false
    ) : AppEvent()

    object OnCloseApp : AppEvent()
    object OnBackScreen : AppEvent()
    class OnShowToast(val content: String, val type: Long = 2000) : AppEvent()

    data class OnResponse<T>(val key : String, val data: T) : AppEvent()
}