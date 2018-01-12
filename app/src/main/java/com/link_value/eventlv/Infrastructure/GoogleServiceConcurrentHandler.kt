package com.link_value.eventlv.Infrastructure

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import java.util.concurrent.Semaphore


/**
 * Created by julienb on 11/01/18.
 */
class GoogleServiceConcurrentHandler(handlerName: String, handlerPriority: Int) : HandlerThread(handlerName, handlerPriority) {

    private fun startHandlerThread(name: String, priority: Int): Looper {
        val semaphore = Semaphore(0)
        val handlerThread = object : HandlerThread(name, priority) {
            override fun onLooperPrepared() {
                semaphore.release()
            }
        }
        handlerThread.start()
        semaphore.acquireUninterruptibly()
        return handlerThread.looper
    }

    override fun quit(): Boolean {
        looper.quit()

        return true
    }

}
