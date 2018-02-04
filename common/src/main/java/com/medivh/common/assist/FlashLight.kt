package com.medivh.common.assist

import android.hardware.Camera
import android.os.Build
import android.os.Handler


/**
 * Created by Medivh on 2018/2/4.
 * <!-- 手电筒 -->
 * Call requires API level 5
 * <uses-permission android:name="android.permission.FLASHLIGHT"/>
 * <uses-permission android:name="android.permission.CAMERA"/>
 */
class FlashLight {

    private var camera: Camera? = null
    private val handler = Handler()

    /**
     * 超过3分钟自动关闭，防止损伤硬件
     */
    private val OFF_TIME = 3 * 60 * 1000

    fun turnOnFlashLight(): Boolean {
        if (camera == null) {
            camera = Camera.open()
            camera!!.startPreview()
            val parameter = camera!!.getParameters()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH)
            } else {
                parameter.set("flash-mode", "torch")
            }
            camera!!.setParameters(parameter)
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed(Runnable { turnOffFlashLight() }, OFF_TIME.toLong())
        }
        return true
    }

    fun turnOffFlashLight(): Boolean {
        if (camera != null) {
            handler.removeCallbacksAndMessages(null)
            val parameter = camera!!.getParameters()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF)
            } else {
                parameter.set("flash-mode", "off")
            }
            camera!!.setParameters(parameter)
            camera!!.stopPreview()
            camera!!.release()
            camera = null
        }
        return true
    }

}