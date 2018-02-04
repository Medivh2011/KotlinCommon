package com.medivh.common.android.async

import android.os.Handler
import android.os.Looper
import com.medivh.common.android.log.Log
import java.util.concurrent.*


/**
 * Created by Medivh on 2018/2/4.
 */
class AsyncExecutor
{
    var handler = Handler(Looper.getMainLooper())
    companion object {
        private val TAG = AsyncExecutor::class.java.simpleName
        private var threadPool: ExecutorService? = null
    }
    constructor(){
        (null)
    }

   constructor(threadPool: ExecutorService?){
        if (AsyncExecutor.threadPool != null) {
            shutdownNow()
        }
        if (threadPool == null) {
            AsyncExecutor.threadPool = Executors.newCachedThreadPool()
        } else {
            AsyncExecutor.threadPool = threadPool
        }
    }

    @Synchronized
    fun shutdownNow() {
        if (threadPool != null && !threadPool!!.isShutdown()) threadPool!!.shutdownNow()
        threadPool = null
    }

    /**
     * 将任务投入线程池执行
     *
     * @param worker
     * @return
     */
    fun <T> execute(worker: Worker<T>): FutureTask<T> {
        val call = object : Callable<T> {
            @Throws(Exception::class)
            override fun call(): T {
                return postResult(worker, worker.doInBackground())
            }
        }
        val task = object : FutureTask<T>(call) {
             override fun done() {
                try {
                    get()
                } catch (e: InterruptedException) {
                    Log.e(TAG, e.toString())
                    worker.abort()
                    postCancel(worker)
                    e.printStackTrace()
                } catch (e: ExecutionException) {
                    Log.e(TAG, e.toString())
                    e.printStackTrace()
                    throw RuntimeException("An error occured while executing doInBackground()", e.cause)
                } catch (e: CancellationException) {
                    worker.abort()
                    postCancel(worker)
                    Log.e(TAG, e.toString())
                    e.printStackTrace()
                }

            }
        }
        threadPool!!.execute(task)
        return task
    }

    /**
     * 将子线程结果传递到UI线程
     *
     * @param worker
     * @param result
     * @return
     */
    private fun <T> postResult(worker: Worker<T>, result: T): T {
        handler.post(Runnable { worker.onPostExecute(result) })
        return result
    }

    /**
     * 将子线程结果传递到UI线程
     *
     * @param worker
     * @return
     */
    private fun postCancel(worker: Worker<*>) {
        handler.post(Runnable { worker.onCanceled() })
    }

    fun <T> execute(call: Callable<T>): FutureTask<T> {
        val task = FutureTask<T>(call)
        threadPool!!.execute(task)
        return task
    }

    abstract class Worker<T> {
        abstract fun doInBackground(): T

        fun onPostExecute(data: T) {}

        fun onCanceled() {}

        fun abort() {}
    }

}