package com.redrock.optimize.prefetchViewHolder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.redrock.optimize.R

/**
 * Author by OkAndGreat
 * Date on 2022/11/1 16:19.
 *
 */
class PrefetchAdapter : RecyclerView.Adapter<PrefetchViewHolder>() {

    private val prefetchViewPool = PrefetchViewPool()
    private var list: List<String> = emptyList()

    init {
        val prefetchItemMap = hashMapOf(
            R.layout.item_recycler_view to 4
        )
        prefetchViewPool.setPrefetchedViewTypeList(prefetchItemMap)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrefetchViewHolder {
        return prefetchViewPool.getPrefetchedItemViewHolder(parent, R.layout.item_recycler_view)
    }

    override fun onBindViewHolder(holder: PrefetchViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setData(list: List<String>){
        this.list = list
        notifyDataSetChanged()
    }

    //Since we need the parent view to which the items will get attached, we will start prefetching on the call of onAttachedToRecyclerView() method.
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        prefetchViewPool.prefetchItemViewHolder(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        prefetchViewPool.detachedFromRecyclerView()
    }
}