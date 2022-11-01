package com.redrock.optimize.prefetchViewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.redrock.optimize.R

/**
 * Author by OkAndGreat
 * Date on 2022/11/1 16:28.
 *
 */
class PrefetchViewHolder(itemView: View, var isPrefetchedViewHolder: Boolean = false) :
    RecyclerView.ViewHolder(itemView) {


    val textView = itemView.findViewById<TextView>(R.id.rv_tv)

    fun onBind(item: String?) {
        textView.text = item
    }
}