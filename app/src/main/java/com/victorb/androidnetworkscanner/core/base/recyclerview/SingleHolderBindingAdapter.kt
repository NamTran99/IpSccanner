package com.victorb.androidnetworkscanner.core.base.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.victorb.androidnetworkscanner.extension.getViewHolderBinding

abstract class SingleHolderBindingAdapter<T : KeyModel, B : ViewDataBinding>(

) : ListAdapter<T, DataBindingViewHolder<T, B>>(KeyCallbackItem<T>()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DataBindingViewHolder<T, B> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val holder = DataBindingViewHolder.from<T, B>(
            getViewHolderBinding(
                this.javaClass,
                layoutInflater,
                parent
            )!!
        )
        initStateViewHolder(holder)
        return holder
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<T, B>, position: Int) {
        onBind(holder.binding, getItem(position), position)
    }

    abstract fun onBind(binding: B, item: T, position: Int)

    open fun initStateViewHolder(holder: DataBindingViewHolder<T, B>) {}
}