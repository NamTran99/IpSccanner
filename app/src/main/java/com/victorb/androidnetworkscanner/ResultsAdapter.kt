package com.victorb.androidnetworkscanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.victorb.androidnetworkscanner.extension.setOnSafeClickListener

class ResultsAdapter() : RecyclerView.Adapter<ResultsAdapter.ViewHolder>() {
    private val dataSet: ArrayList<Device> = arrayListOf();

    /**
     * Get the needed views as objects
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val hostnameTextView: TextView = view.findViewById(R.id.textview_hostname)
        val ipTextView: TextView = view.findViewById(R.id.textview_ip)
    }

    var onSizeChange : ((Int) -> Unit)?= null
    var onItemClick : ((Device) -> Unit)? = null

    /**
     * Sets the layout to use for one item
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_results,
                parent,
    false)
    )

    /**
     * Bind the text views to data
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ipTextView.text = dataSet[position].ip
        holder.hostnameTextView.text = dataSet[position].hostname

        holder.itemView.setOnSafeClickListener {
            onItemClick?.invoke(dataSet[position])
        }
    }

    /**
     * Get item count
     */
    override fun getItemCount() =
        dataSet.size

    /**
     * Add an item to the dataset
     */
    fun addItem(hostname: String, ip: String) {
        dataSet.add(Device(hostname, ip))
        runOnMainThread {
            onSizeChange?.invoke(dataSet.size)
            notifyItemInserted(dataSet.size)
        }
    }

    /**
     * Clears the list, for refreshing for example
     */
    fun clear() {
        dataSet.clear()
        runOnMainThread {
            onSizeChange?.invoke(0)
            notifyDataSetChanged()
        }
    }
}