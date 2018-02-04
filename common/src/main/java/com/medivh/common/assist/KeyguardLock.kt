package com.medivh.common.assist

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.content.Context.KEYGUARD_SERVICE
import android.util.Log


/**
 * Created by 98132 on 2018/2/4.
 * <!-- 解锁 -->
 * require <uses-permission android:name="android.permission.DISABLE_KEYGUARD"/>
 */
class KeyguardLock
{

    private lateinit var keyguardManager: KeyguardManager
    private var keyguardLock: KeyguardManager.KeyguardLock? = null

    fun KeyguardLock(context: Context, tag: String){
        //获取系统服务
        keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        //初始化键盘锁，可以锁定或解开键盘锁
        keyguardLock = keyguardManager.newKeyguardLock(tag)
    }

    /**
     * Call requires API level 16
     */
    fun isKeyguardLocked(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Log.e("Log : ", "can not call isKeyguardLocked if SDK_INT < 16 ")
            return false
        } else {
            return keyguardManager.isKeyguardLocked
        }

    }

    /**
     * Call requires API level 16
     */
    fun isKeyguardSecure(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Log.e("Log : ", "can not call isKeyguardSecure if SDK_INT < 16 ")
            return false
        } else {
            return keyguardManager.isKeyguardSecure
        }
    }

    fun inKeyguardRestrictedInputMode(): Boolean {
        return keyguardManager.inKeyguardRestrictedInputMode()
    }

    @SuppressLint("MissingPermission")
    fun disableKeyguard() {
        keyguardLock!!.disableKeyguard()
    }

    @SuppressLint("MissingPermission")
    fun reenableKeyguard() {
        keyguardLock!!.reenableKeyguard()
    }

    @SuppressLint("MissingPermission")
    fun release() {
        if (keyguardLock != null) {
            //禁用显示键盘锁定
            keyguardLock!!.reenableKeyguard()
        }
    }

    fun getKeyguardManager(): KeyguardManager {
        return keyguardManager
    }

    fun setKeyguardManager(keyguardManager: KeyguardManager) {
        this.keyguardManager = keyguardManager
    }

    fun getKeyguardLock(): KeyguardManager.KeyguardLock? {
        return keyguardLock
    }

    fun setKeyguardLock(keyguardLock: KeyguardManager.KeyguardLock) {
        this.keyguardLock = keyguardLock
    }

}