package com.victorb.androidnetworkscanner.core.base.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractDataBindingViewHolder<out T : ViewDataBinding>(
    val binding: T,
) : RecyclerView.ViewHolder(binding.root) {
}