package com.victorb.androidnetworkscanner.core.base.recyclerview

import androidx.recyclerview.widget.DiffUtil

abstract class BaseCallbackItem<MODEL: KeyModel>: DiffUtil.ItemCallback<MODEL>(){
    override fun areItemsTheSame(oldItem: MODEL, newItem: MODEL) = oldItem === newItem

    override fun areContentsTheSame(oldItem: MODEL, newItem: MODEL): Boolean {
        return oldItem.toString() == newItem.toString()
    }
}