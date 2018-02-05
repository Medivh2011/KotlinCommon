package com.medivh.common.basebindingadapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.util.SparseIntArray
import android.view.ViewGroup

/**
 * Created by Medivh on 2017/12/27.
 */

abstract class BaseMuliteItemAdapter<T : ItemViewType>(context: Context) : BaseBindingAdapter<T>(context) {

    private var layouts: SparseIntArray? = null

    protected fun addItemType(type: Int, @LayoutRes layoutResId: Int) {
        if (layouts == null) {
            layouts = SparseIntArray()
        }
        layouts!!.put(type, layoutResId)
    }

    private fun getLayoutId(viewType: Int): Int {
        return layouts!!.get(viewType, TYPE_NOT_FOUND)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<*> {
        return BindingViewHolder(DataBindingUtil.inflate<ViewDataBinding>(mLayoutInflater, getLayoutId(viewType), parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        val t = list!![position]
        return t.viewType
    }

    companion object {

        val TYPE_NOT_FOUND = -404
    }

}
