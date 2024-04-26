package com.victorb.androidnetworkscanner.ui.recycler

import com.victorb.androidnetworkscanner.Device
import com.victorb.androidnetworkscanner.R
import com.victorb.androidnetworkscanner.core.base.recyclerview.SingleHolderBindingAdapter
import com.victorb.androidnetworkscanner.data.local.model.PingStatusLocal
import com.victorb.androidnetworkscanner.databinding.ItemPingResultBinding

class PingResultAdapter :
    SingleHolderBindingAdapter<PingStatusLocal, ItemPingResultBinding>() {
    var device: Device? = null
    fun setHostData(data: Device?) {
        device = data
    }

    override fun onBind(binding: ItemPingResultBinding, item: PingStatusLocal, position: Int) {
        val context = binding.root.context
        binding.apply {
            tvReplyFrom.setText(
                context.getString(
                    R.string.reply_from_ip,
                    device?.ip ?: device?.hostname ?: "Unknown"
                )
            )
        }
    }

    fun addMoreItem(data: List<PingStatusLocal>) {
        val a = currentList.toMutableList()
        a.addAll(data)
        submitList(a)
    }
}