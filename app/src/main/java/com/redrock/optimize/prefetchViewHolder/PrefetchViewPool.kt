package com.redrock.optimize.prefetchViewHolder

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import java.util.*

/**
 * Author by OkAndGreat
 * Date on 2022/11/1 16:07.
 *
 */
class PrefetchViewPool {

    companion object {
        const val TAG = "PrefetchViewPool"
    }

    private var prefetchViewTypeList = HashMap<Int, Int>()
    private var prefetchItemViewHolderMap = HashMap<Int, Queue<PrefetchViewHolder>>()
    private var isAdapterDetached: Boolean = false

    fun setPrefetchedViewTypeList(viewTypeList: HashMap<Int, Int>) {
        prefetchViewTypeList.clear()
        prefetchViewTypeList.putAll(viewTypeList)
    }

    fun getPrefetchedItemViewHolder(parent: ViewGroup, layoutId: Int): PrefetchViewHolder {
        return prefetchItemViewHolderMap[layoutId]?.poll() ?: initInflateItem(parent, layoutId)
    }

    fun prefetchItemViewHolder(parent: ViewGroup) {
        if (prefetchViewTypeList.isEmpty()) return

        prefetchViewTypeList.forEach { viewTypeToCount ->
            if (isAdapterDetached) return
            if (prefetchItemViewHolderMap[viewTypeToCount.key] != null) return@forEach

            initAsyncInflateItem(parent, viewTypeToCount.key, viewTypeToCount.value)
        }
    }

    private fun initInflateItem(parent: ViewGroup, layoutId: Int): PrefetchViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        return PrefetchViewHolder(layoutInflater.inflate(layoutId, parent, false))
    }

    private fun initAsyncInflateItem(parent: ViewGroup, layoutId: Int, count: Int) {

        Log.d(
            TAG,
            "initAsyncInflateItem: start async inflate items... layoutId is $layoutId count is $count"
        )

        val layoutInflater = AsyncLayoutInflater(parent.context)

        for (index in 0 until count) {
            if (isAdapterDetached) return
            try {
                layoutInflater.inflate(layoutId, parent) { view, _, _ ->
                    run {

                        val prefetchViewHolderList: Queue<PrefetchViewHolder> =
                            prefetchItemViewHolderMap[layoutId] ?: ArrayDeque()
                        val viewHolder = PrefetchViewHolder(view)

                        prefetchViewHolderList.add(viewHolder)
                        prefetchItemViewHolderMap[layoutId] = prefetchViewHolderList

                        Log.d(TAG, "initAsyncInflateItem: async success! layoutId is $layoutId index is $index")
                    }
                }
            } catch (e: Throwable) {
                Log.d(TAG, "initAsyncInflateItem: async failed layoutId is $layoutId index is $index")
            }

        }

    }

    fun detachedFromRecyclerView() {
        isAdapterDetached = true
    }

}