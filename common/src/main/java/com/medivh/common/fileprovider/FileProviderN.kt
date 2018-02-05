package com.medivh.common.fileprovider

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build

import java.io.File


object FileProviderN {

    fun getUriFromFile(context: Context, file: File): Uri? {
        var fileUri: Uri? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = getUriForFileN(context, file)
        } else {
            fileUri = Uri.fromFile(file)
        }
        return fileUri
    }


   private fun getUriForFileN(context: Context, file: File): Uri {
        return FitFileProvider.getUriForFile(context.applicationContext,
                context.applicationContext.packageName + ".fileprovider", file)

    }


    fun setIntentDataAndType(context: Context,
                             intent: Intent,
                             type: String,
                             file: File,
                             writeAble: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(getUriFromFile(context, file), type)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type)
        }
    }


    fun setIntentData(context: Context,
                      intent: Intent,
                      file: File,
                      writeAble: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.data = getUriFromFile(context, file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        } else {
            intent.data = Uri.fromFile(file)
        }
    }


    fun grantPermissions(context: Context, intent: Intent, uri: Uri, writeAble: Boolean) {

        var flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
        if (writeAble) {
            flag = flag or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        }
        intent.addFlags(flag)
        val resInfoList = context.packageManager
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(packageName, uri, flag)
        }
    }


}
