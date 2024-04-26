package com.victorb.androidnetworkscanner.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.victorb.androidnetworkscanner.R
import com.victorb.androidnetworkscanner.databinding.ViewToolBarBinding
import com.victorb.androidnetworkscanner.extension.setOnSafeClickListener
import com.victorb.androidnetworkscanner.extension.show

class CustomToolBar (
    context: Context, attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {
    private val binding: ViewToolBarBinding
    var onStartIconClicked: () -> Unit = {}
    var onScanClicked: (View) -> Unit = {}

    private var  endButtonContent : String = ""
        set(value) {
            binding.btScan.text = value
            field = value
        }

    var title: String = ""
        set(value) {
            binding.tvTitle.text = value
            field = value
        }

    var isShowEndButton: Boolean = true
        set(value) {
            binding.btScan.show (value)
            field = value
        }


    init {
        binding = ViewToolBarBinding.inflate(LayoutInflater.from(context), this, true)

        context.withStyledAttributes(attrs, R.styleable.CustomToolBar) {
            title = getString(R.styleable.CustomToolBar_customToolBarTitle)?: ""
            isShowEndButton = getBoolean(R.styleable.CustomToolBar_customToolBarShowEndButton, true)
            endButtonContent = getString(R.styleable.CustomToolBar_customToolBarEndButtonContent)?: context.getString(R.string.scan)
        }

        binding.iconMenu.setOnSafeClickListener {
            onStartIconClicked()
        }

        binding.btScan.setOnSafeClickListener {
            onScanClicked(it)
        }
    }
}