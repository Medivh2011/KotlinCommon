package com.medivh.common.android.log

import java.io.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Medivh on 2018/2/4.
 */
object LogReader : Thread() {

    val TAG = "LogReader"
    val LOG_FILE_PATH = "/bonglog.txt"
    val LOG_ROOT_PATH = "/sdcard"

    var open = true
    private var instance: LogReader? = null
    private var mLogcatProc: Process? = null

    private var mReader: BufferedReader? = null
    private var packageName = "*"

    fun startCatchLog(packageName: String) {
        if (!open) return
        if (instance == null) {
            instance = LogReader
            instance!!.packageName = packageName
            instance!!.start()
        }
    }

    fun stopCatchLog() {
        if (!open) return
        if (mLogcatProc != null) {
            mLogcatProc!!.destroy()
            mLogcatProc = null
        }
    }

    override fun run() {
        Log.i(TAG, "log reader(catcher) is running..---------------------------")
        var bw: BufferedWriter? = null
        try {
            mLogcatProc = Runtime.getRuntime().exec("logcat $packageName:I")
            mReader = BufferedReader(InputStreamReader(mLogcatProc!!.inputStream))

            // 打印系统信息。
            logSystemInfo()

            var line: String?
            val file = File(LOG_ROOT_PATH + LOG_FILE_PATH)
            if (file.exists() && isFileSizeOutof10M(file)) {
                file.delete()
            }
            if (file.exists()) {
                println("log file size is :" + FormatFileSize(file.length()))
            }
            val fw = FileWriter(file, true)
            bw = BufferedWriter(fw)
            while ((mReader!!.readLine()) != null) {
                line = mReader!!.readLine()
                bw!!.append(line)
                bw!!.newLine()
                bw!!.flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            Log.i(TAG, "Log reader(catcher) and bufferwriter has closed. ------------------")
            try {
                if (mReader != null) {
                    mReader!!.close()
                    mReader = null
                }
                if (bw != null) {
                    bw!!.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            instance = null
        }

    }

    fun FormatFileSize(fileS: Long): String {// 转换文件大小
        val df = DecimalFormat("#.00")
        var fileSizeString = ""
        if (fileS < 1024) {
            fileSizeString = df.format(fileS.toDouble()) + "B"
        } else if (fileS < 1048576) {
            fileSizeString = df.format(fileS.toDouble() / 1024) + "K"
        } else if (fileS < 1073741824) {
            fileSizeString = df.format(fileS.toDouble() / 1048576) + "M"
        } else {
            fileSizeString = df.format(fileS.toDouble() / 1073741824) + "G"
        }
        return fileSizeString
    }

    /**
     * 判断文件是否大于10M。
     *
     * @param file
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun isFileSizeOutof10M(file: File?): Boolean {
        return if (file == null) false else file!!.length() >= 10485760
    }

    fun logSystemInfo() {
        val date = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val time = dateFormat.format(date)
        android.util.Log.w("system", "New Start $$$$$$$$$$$$$$###########   $time############$$$$$$$$$$$$$$$")
        android.util.Log.w("system", "android.os.Build.BOARD:" + android.os.Build.BOARD)
        android.util.Log.w("system", "android.os.Build.DEVICE:" + android.os.Build.DEVICE)
        android.util.Log.w("system", "android.os.Build.MANUFACTURER:" + android.os.Build.MANUFACTURER)
        android.util.Log.w("system", "android.os.Build.MODEL:" + android.os.Build.MODEL)
        android.util.Log.w("system", "android.os.Build.PRODUCT:" + android.os.Build.PRODUCT)
        android.util.Log.w("system", "android.os.Build.VERSION.CODENAME:" + android.os.Build.VERSION.CODENAME)
        android.util.Log.w("system", "android.os.Build.VERSION.RELEASE:" + android.os.Build.VERSION.RELEASE)
        //android.util.Log.w("system", "android.os.Build.VERSION.SDK:"
        //        + android.os.Build.VERSION.SDK);
    }
}