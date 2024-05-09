package com.victorb.androidnetworkscanner.ui.recycler

import android.util.Log
import com.victorb.androidnetworkscanner.core.base.recyclerview.SingleHolderBindingAdapter
import com.victorb.androidnetworkscanner.data.local.model.InformationIPModel
import com.victorb.androidnetworkscanner.data.local.model.InformationType
import com.victorb.androidnetworkscanner.databinding.ItemInformationIpBinding
import com.victorb.androidnetworkscanner.extension.hide
import com.victorb.androidnetworkscanner.ui.fragment.FragmentIPInformationDetail.Companion.LO_COUNTRY

class ItemInformationAdapter :
    SingleHolderBindingAdapter<InformationIPModel, ItemInformationIpBinding>() {
    override fun onBind(
        binding: ItemInformationIpBinding,
        item: InformationIPModel,
        position: Int
    ) {
        binding.apply {
            tvTitle.text = item.title

            tvContent.hide(
                item.type == InformationType.Icon
            )
            imgIcon.hide(
                item.type == InformationType.Text
            )
            tvContent.text = item.content ?: "N/A"
            item.icon?.let { imgIcon.setImageResource(it) }
        }
    }

    override fun submitList(list: MutableList<InformationIPModel>?) {
        if (list.isNullOrEmpty()) return
        val copyList = list.map {
            it.copy()
        }
        super.submitList(copyList)
    }

    fun updateItem(data: InformationIPModel){
        val a = currentList.toMutableList()

        val index = a.indexOfFirst {
            it.id == data.id
        }

        if(index != -1){
            a[index] = data
        }
        submitList(a)
    }
}