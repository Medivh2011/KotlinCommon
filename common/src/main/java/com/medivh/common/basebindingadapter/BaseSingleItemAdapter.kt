package com.medivh.common.basebindingadapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.view.ViewGroup

/**
 * Created by Medivh on 2018/2/5.
 */

abstract class BaseSingleItemAdapter<T>(context: Context, @param:LayoutRes private var layoutRes: Int) : BaseBindingAdapter<T>(context) {
    fun setLayoutRes(@LayoutRes layoutRes: Int) {
        this.layoutRes = layoutRes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<*> {
        return BindingViewHolder(DataBindingUtil.inflate<ViewDataBinding>(mLayoutInflater, layoutRes, parent, false))
    }

}