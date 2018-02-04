package com.medivh.common.crash

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Process
import android.util.Log
import java.io.*
import java.lang.Thread.UncaughtExceptionHandler
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Medivh on 2018/2/3.
 */
class CrashHandler : UncaughtExceptionHandler {

    private val TAG = "Medivh"
    private val DEBUG = true
    private val FILE_NAME = "crash"
    private val FILE_NAME_SUFFIX = ".txt"
    private var mCallback: HttpReportCallback? = null
    private var mDefaultCrashHandler: UncaughtExceptionHandler? = null
    private var mContext: Context? = null
    private constructor(){}
    companion object {
        private val sInstance : CrashHandler = CrashHandler()
        fun getInstance(): CrashHandler {
            return sInstance
        }
    }
    fun init(context: Context) {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
        mContext = context.applicationContext
    }
    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息。
     */
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        try {
            // 导出异常信息到SD卡中
            val file = dumpExceptionToSDCard(ex)
            //			uploadExceptionToServer(file);
            // 这里可以通过网络上传异常信息到服务器，便于开发人员分析日志从而解决bug
        } catch (e: IOException) {
            e.printStackTrace()
        }

        ex.printStackTrace()

        // 如果系统提供了默认的异常处理器，则交给系统去结束我们的程序，否则就由我们自己结束自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler?.uncaughtException(thread, ex)
        } else {
            Process.killProcess(Process.myPid())
        }
    }
    @Throws(IOException::class)
    private fun dumpExceptionToSDCard(ex: Throwable): File? {
        // 如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            if (DEBUG) {
                Log.e(TAG, "sdcard unmounted,skip dump exception")
                return null
            }
        }
        val dir = getDiskCacheDir(mContext, "Crash")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val current = System.currentTimeMillis()
        val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(Date(current))
        val file = File(getDiskCacheDir(mContext, "Crash").path +File.separator+ FILE_NAME + time + FILE_NAME_SUFFIX)
        try {
            val pw = PrintWriter(BufferedWriter(FileWriter(file)))
            pw.println(time)
            dumpPhoneInfo(pw)
            pw.println()
            ex.printStackTrace(pw)
            pw.close()
        } catch (e: Exception) {
            Log.e(TAG, "dump crash info failed")
        }
        return file
    }
    @Throws(PackageManager.NameNotFoundException::class)
    private fun dumpPhoneInfo(pw: PrintWriter) {
        val pm = mContext?.getPackageManager()
        val pi = pm?.getPackageInfo(mContext?.getPackageName(),
                PackageManager.GET_ACTIVITIES)
        pw.print("App Version: ")
        pw.print(pi?.versionName)
        pw.print('_')
        pw.println(pi?.versionCode)

        // android版本号
        pw.print("OS Version: ")
        pw.print(Build.VERSION.RELEASE)
        pw.print("_")
        pw.println(Build.VERSION.SDK_INT)

        // 手机制造商
        pw.print("Vendor: ")
        pw.println(Build.MANUFACTURER)

        // 手机型号
        pw.print("Model: ")
        pw.println(Build.MODEL)

        // cpu架构
        pw.print("CPU ABI: ")
        pw.println(Build.CPU_ABI)
    }

    private fun uploadExceptionToServer(file: File) {
        // 上传服务器
        mCallback?.uploadException2remote(file)

    }

    // 设置上传到服务器的接口回调
    fun setCrashReporter(callback: HttpReportCallback) {
        this.mCallback = callback
    }

    private fun getDiskCacheDir(context: Context?, uniqueName: String): File {
        val cachePath: String
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
            cachePath = context?.externalCacheDir!!.path
        } else {
            cachePath = context?.cacheDir!!.path
        }
        return File(cachePath + File.separator + uniqueName)
    }

}