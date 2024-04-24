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

class CustomToolBar (
    context: Context, attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {
    private val binding: ViewToolBarBinding
    var onStartIconClicked: () -> Unit = {}
    var onScanClicked: (View) -> Unit = {}

    var title: String = ""
        set(value) {
            binding.tvTitle.text = value
            field = value
        }



    init {
        binding = ViewToolBarBinding.inflate(LayoutInflater.from(context), this, true)

        context.withStyledAttributes(attrs, R.styleable.CustomToolBar) {
            title = getString(R.styleable.CustomToolBar_customToolBarTitle)?: ""
        }

        binding.iconMenu.setOnSafeClickListener {
            onStartIconClicked()
        }

        binding.btScan.setOnSafeClickListener {
            onScanClicked(it)
        }
    }
}