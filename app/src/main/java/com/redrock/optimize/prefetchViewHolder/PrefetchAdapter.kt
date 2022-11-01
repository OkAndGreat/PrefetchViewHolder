package com.redrock.optimize.prefetchViewHolder

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.redrock.optimize.R

/**
 * Author by OkAndGreat
 * Date on 2022/11/1 16:19.
 */
abstract class PrefetchAdapter<vh : PrefetchViewHolder, vp : PrefetchViewPool<vh>>(@LayoutRes open val layoutId: Int) :
    RecyclerView.Adapter<vh>() {

    private val prefetchViewPool = getExactlyPrefetchViewPool()

    init {
        //       PrefetchViewPool.addPrefetchedViewType(R.layout.item_recycler_view to 4)

        prefetchViewPool.addPrefetchedViewType(R.layout.item_recycler_view to 4)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vh {
        return prefetchViewPool.getPrefetchedItemViewHolder(parent, layoutId)
    }


    //Since we need the parent view to which the items will get attached, we will start prefetching on the call of onAttachedToRecyclerView() method.
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        prefetchViewPool.prefetchItemViewHolder(recyclerView, layoutId)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        prefetchViewPool.detachedFromRecyclerView()
    }

    abstract fun getExactlyPrefetchViewPool(): vp
}