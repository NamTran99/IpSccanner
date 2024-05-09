package com.victorb.androidnetworkscanner.ui.recycler

import com.victorb.androidnetworkscanner.R
import com.victorb.androidnetworkscanner.core.base.recyclerview.SingleHolderBindingAdapter
import com.victorb.androidnetworkscanner.data.local.model.OptionDrawerModel
import com.victorb.androidnetworkscanner.databinding.ItemMenuDrawerLayoutBinding
import com.victorb.androidnetworkscanner.extension.setOnSafeClickListener

class DrawerOptionItemAdapter :
    SingleHolderBindingAdapter<OptionDrawerModel, ItemMenuDrawerLayoutBinding>() {
    private var selectedIndex: Int = -1
    var onItemClick: ((OptionDrawerModel) -> Unit)? = null
    override fun onBind(
        binding: ItemMenuDrawerLayoutBinding,
        item: OptionDrawerModel,
        position: Int
    ) {
        val context = binding.root.context
        binding.apply {
            imgIcon.setImageResource(item.icon)
            tvName.text = item.name
            tvName.setTextColor(
                context.resources.getColor(
                    if (item.isSelected)
                        R.color.color_item_selected
                    else R.color.color_text_unselect, null
                )
            )
            layoutDrawerOption.isSelected = item.isSelected
            root.setOnSafeClickListener {
                if (item.isSelected) return@setOnSafeClickListener
                item.isSelected = true
                if (selectedIndex != -1) {
                    currentList[selectedIndex].isSelected = false
                    notifyItemChanged(selectedIndex)
                }
                notifyItemChanged(position)
                selectedIndex = position
                onItemClick?.invoke(item)
            }
        }
    }

    fun setListItem(list: List<OptionDrawerModel>) {
        submitList(list)
        selectedIndex = list.indexOfFirst {
            it.isSelected
        }
    }

}