package com.victorb.androidnetworkscanner.core.base.recyclerview

/**
 * All e-wallet model have to extends this Model
 * otherwise, you cannot use e-wallet base adapter
 */
interface KeyModel {
    val identity: String
}