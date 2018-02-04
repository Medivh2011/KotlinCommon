package com.medivh.common.android.log


/**
 * Created by Medivh on 2018/2/4.
 */
 object  Log {
    /**
     * isPrint: print switch, true will print. false not print
     */
    var isPrint = true
    private var defaultTag = "Log"


    fun setTag(tag: String) {
        defaultTag = tag
    }
    fun i(m: String?): Int {
        return if (isPrint && m != null) android.util.Log.i(defaultTag, m) else -1
    }

    /**
     * ******************** Log **************************
     */
     fun v(tag: String, msg: String?): Int {
        return if (isPrint && msg != null) android.util.Log.v(tag, msg) else -1
    }

    fun d(tag: String, msg: String?): Int {
        return if (isPrint && msg != null) android.util.Log.d(tag, msg) else -1
    }

    fun i(tag: String, msg: String?): Int {
        return if (isPrint && msg != null) android.util.Log.i(tag, msg) else -1
    }

    fun w(tag: String, msg: String?): Int {
        return if (isPrint && msg != null) android.util.Log.w(tag, msg) else -1
    }

    fun e(tag: String, msg: String?): Int {
        return if (isPrint && msg != null) android.util.Log.e(tag, msg) else -1
    }
    private fun getLogMessage(vararg msg: Any): String {
        if (msg != null && msg.size > 0) {
            val sb = StringBuilder()
            for (s in msg) {
                if (s != null) {
                    sb.append(s.toString())
                }
            }
            return sb.toString()
        }
        return ""
    }






}