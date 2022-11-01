package com.redrock.optimize.prefetchViewHolder


import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.collection.ArrayMap
import java.util.*

/**
 * Author by OkAndGreat
 * Date on 2022/11/1 16:07.
 *
 */
abstract class PrefetchViewPool<vh : PrefetchViewHolder> {

    companion object {
        const val TAG = "PrefetchViewPool"
    }


    private var prefetchViewTypeList = SparseArray<Int>()
    private var prefetchItemViewHolderMap = ArrayMap<Int, Queue<vh>>()
    private var isAdapterDetached: Boolean = false

    fun addPrefetchedViewType(prefetchViewTypePair: Pair<Int, Int>) {
        prefetchViewTypeList[prefetchViewTypePair.first] = prefetchViewTypePair.second
    }

    fun getPrefetchedItemViewHolder(
        parent: ViewGroup,
        layoutId: Int,
    ): vh {
        return prefetchItemViewHolderMap[layoutId]?.poll() ?: syncInflateItem(parent, layoutId)
    }

    //This should be called when adapter attached to the RecyclerView
    fun prefetchItemViewHolder(parent: ViewGroup, layoutId: Int) {
        if (prefetchViewTypeList[layoutId] == null || isAdapterDetached) return

        val needCount = prefetchViewTypeList[layoutId]
        val currCount = prefetchItemViewHolderMap[layoutId]?.size ?: 0
        val preloadCount = needCount - currCount
        //no need to preload as needCount is satisfied.
        if (preloadCount <= 0) return

        Log.d(
            TAG,
            "prefetchItemViewHolder: start async inflate items... layoutId is $layoutId needCount is $needCount currCount is $currCount"
        )

        //has a limit of 10 tasks.todo:change from AsyncLayoutInflater to OkLayoutInflater
        val layoutInflater = AsyncLayoutInflater(parent.context)

        for (index in 0 until preloadCount) {
            try {
                layoutInflater.inflate(layoutId, parent) { view, _, _ ->
                    run {
                        val prefetchViewHolderList =
                            prefetchItemViewHolderMap[layoutId] ?: ArrayDeque()
                        val viewHolder = getViewHolder(parent, layoutId, view)

                        prefetchViewHolderList.add(viewHolder)
                        prefetchItemViewHolderMap[layoutId] = prefetchViewHolderList

                        Log.d(
                            TAG,
                            "initAsyncInflateItem: async load success! layoutId is $layoutId index is $index"
                        )
                    }
                }
            } catch (e: Throwable) {
                Log.d(
                    TAG,
                    "initAsyncInflateItem: async load failed... probably because the AsyncLayoutInflater has a limit of 10 tasks on preloading. layoutId is $layoutId index is $index e:$e"
                )
                //try use sync layoutInflater

            } finally {
                val syncLayoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
                val view = syncLayoutInflater.inflate(layoutId, parent, false)

                val prefetchViewHolderList = prefetchItemViewHolderMap[layoutId] ?: ArrayDeque()
                val viewHolder = getViewHolder(parent, layoutId, view)

                prefetchViewHolderList.add(viewHolder)
                prefetchItemViewHolderMap[layoutId] = prefetchViewHolderList
            }
        }

    }

//    private fun syncInflateItem(parent: ViewGroup, layoutId: Int): vh {
//        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
//        return vh(
//            layoutInflater.inflate(
//                layoutId,
//                parent,
//                false
//            )
//        )
//    }

    abstract fun getViewHolder(parent: ViewGroup, layoutId: Int, view: View): vh

    abstract fun syncInflateItem(parent: ViewGroup, layoutId: Int): vh

    fun detachedFromRecyclerView() {
        isAdapterDetached = true
    }

}