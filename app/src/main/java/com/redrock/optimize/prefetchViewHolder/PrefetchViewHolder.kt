package com.redrock.optimize.prefetchViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Author by OkAndGreat
 * Date on 2022/11/1 16:28.
 *
 */
abstract class PrefetchViewHolder(
    itemView: View,
    private val isPrefetchedViewHolder: Boolean = false
) :
    RecyclerView.ViewHolder(itemView){

    fun isPrefetch() = isPrefetchedViewHolder
}