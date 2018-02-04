package com.medivh.common.crash

import java.io.File
/**
 * Created by Medivh on 2018/2/3.
 */
interface HttpReportCallback
{
    fun uploadException2remote(file : File)
}