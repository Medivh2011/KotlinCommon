package com.medivh.common.basebindingadapter


import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater

import java.util.ArrayList

/**
 * Created by Medivh on 2017/12/27.
 */

abstract class BaseBindingAdapter<T>(context: Context) : RecyclerView.Adapter<BindingViewHolder<*>>() {

    protected val mLayoutInflater: LayoutInflater

    private val mList: MutableList<T>?

    val list: List<T>?
        get() = mList

    init {
        mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mList = ArrayList()
    }

    override fun onBindViewHolder(holder: BindingViewHolder<*>, position: Int) {
        val t = mList!![position]
        setItemVariable(holder.binding, t)
        holder.binding.executePendingBindings()
        setItemClickEvent(holder, t)
    }

    protected abstract fun setItemVariable(binding: ViewDataBinding, t: T)

    protected fun setItemClickEvent(holder: BindingViewHolder<*>, t: T) {}
    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    fun add(index: Int, t: T) {
        if (index > mList!!.size) {
            return
        }
        mList.add(index, t)
        notifyItemInserted(index)
    }

    fun add(t: T) {
        mList!!.add(t)
        notifyItemInserted(mList.size)
    }

    fun addAtFirst(t: T) {
        mList!!.add(0, t)
        notifyItemInserted(0)
    }

    fun addAll(tList: List<T>) {
        mList!!.addAll(tList)
    }

    fun remove(index: Int) {
        if (index >= mList!!.size) {
            return
        }
        mList.removeAt(index)
        notifyDataSetChanged()
    }

    fun removeFirst() {
        if (mList!!.size == 0) {
            return
        }
        mList.removeAt(0)
    }

    fun removeLast() {
        mList!!.removeAt(mList.size - 1)
    }


    fun removeAll() {
        mList!!.clear()
    }

}