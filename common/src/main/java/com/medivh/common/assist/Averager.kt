package com.medivh.common.assist

import android.util.Log


/**
 * Created by Medivh on 2018/2/4.
 *  用以统计平均数
 */
class Averager
{
    private val TAG = "Averager"
    private val numList = ArrayList<Number>()
    /**
     * 添加一个数字
     *
     * @param num
     */
    @Synchronized
    fun add(num: Number) {
        numList.add(num)
    }

    /**
     * 清除全部
     */
    fun clear() {
        numList.clear()
    }

    /**
     * 返回参与均值计算的数字个数
     *
     * @return
     */
    fun size(): Number {
        return numList.size
    }

    /**
     * 获取平均数
     *
     * @return
     */
    fun getAverage(): Number {
        if (numList.size === 0) {
            return 0
        } else {
            var sum: Float? = 0f
            var i = 0
            val size = numList.size
            while (i < size) {
                sum = sum!!.toFloat() + numList[i].toFloat()
                i++
            }
            return sum!! / numList.size
        }
    }
    /**
     * 打印数字列
     * @return
     */
    fun print(): String {
        val str = "PrintList(" + size() + "): " + numList
        Log.e(TAG, str)
        return str
    }

}