package com.victorb.androidnetworkscanner.extension

open class SingletonHolder<out T>(private val constructor: () -> T) {

    @Volatile
    private var instance: T? = null

    fun getInstance(): T =
        instance ?: synchronized(this) {
            instance ?: constructor().also { instance = it }
        }

    fun clearInstance(){
        instance = null
    }
}

open class SingletonHolderWithParam<out T,in R>(private val constructor: (R) -> T) {

    @Volatile
    private var instance: T? = null

    fun getInstance(arg : R): T =
        instance ?: synchronized(this) {
            instance ?: constructor(arg).also { instance = it }
        }

    fun clearInstance(){
        instance = null
    }
}