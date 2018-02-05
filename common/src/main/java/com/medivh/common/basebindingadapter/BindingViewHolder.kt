package com.medivh.common.basebindingadapter

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * Created by Medivh on 2017/12/26.
 */

class BindingViewHolder<T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)
