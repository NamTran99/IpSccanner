package com.victorb.androidnetworkscanner.ui.recycler

import android.util.Log
import com.marsounjan.icmp4a.Icmp
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
            when (item.result) {
                is Icmp.PingResult.Success -> {
                    imgStatusDot.setColorFilter(context.resources.getColor(R.color.color_dot_active,null))
                    tvContent.text = context.getString(
                        R.string.reply_content_success,
                        item.result.packetSize.toString(),
                        item.result.ms.toString()
                    )
                }

                is Icmp.PingResult.Failed ->{
                    imgStatusDot.setColorFilter(context.resources.getColor(R.color.color_dot_inactive,null))
                    tvContent.text = context.getString(
                        R.string.reply_content_failure,
                        item.result.message
                    )
                }

            }

        }
    }

    fun addMoreItem(data: List<PingStatusLocal>, onChangeNewList: (List<PingStatusLocal>) -> Unit) {
        val a = currentList.toMutableList()
        a.addAll(data)
        onChangeNewList.invoke(a)
        submitList(a)
    }
}