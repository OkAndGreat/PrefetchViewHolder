package com.redrock.optimize

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.redrock.optimize.prefetchViewHolder.PrefetchAdapter
import com.redrock.optimize.prefetchViewHolder.PrefetchViewHolder
import com.redrock.optimize.prefetchViewHolder.PrefetchViewPool

/**
 * Author by OkAndGreat
 * Date on 2022/11/1 21:05.
 *
 */
class TestAdapter(@LayoutRes override val layoutId: Int) :
    PrefetchAdapter<TestViewHolder, TestViewPool>(layoutId) {

    private var list: List<String> = emptyList()

//    override fun onBindViewHolder(holder: PrefetchViewHolder, position: Int) {
//        holder.onBind(list[position])
//    }

    override fun getItemCount(): Int = list.size

    fun setData(list: List<String>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getExactlyPrefetchViewPool(): TestViewPool {
        return TestViewPool()
    }

}

class TestViewHolder(itemView: View) : PrefetchViewHolder(itemView) {

    fun onBind() {

    }
}

class TestViewPool : PrefetchViewPool<TestViewHolder>() {
    override fun getViewHolder(parent: ViewGroup, layoutId: Int, view: View): TestViewHolder {
        return TestViewHolder(View(parent.context))
    }

    override fun syncInflateItem(parent: ViewGroup, layoutId: Int): TestViewHolder {
        return TestViewHolder(View(parent.context))
    }

}