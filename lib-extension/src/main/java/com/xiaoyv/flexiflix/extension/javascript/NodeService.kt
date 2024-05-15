package com.xiaoyv.flexiflix.extension.javascript

import MyWorker
import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.Process
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.locks.ReentrantLock

/**
 * [NodeService]
 *
 * @author why
 * @since 5/13/24
 */
class NodeService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val i = NodeExtension.startNodeWithArguments(
            arrayOf(
                "node", "-e",
                "setTimeout(()=>{console.log(\"nodejs\");},10000)"
            )
        )

        Log.e("NodeService", "return:$i, pid:${Process.myPid()}")
        stopSelf()
        Process.killProcess(Process.myPid())
    }

    companion object {
        private val lock: ReentrantLock = ReentrantLock()

        @JvmStatic
        fun run(context: Context, args: Array<String>) {
            lock.lock()
            try {
                start(context)
            } finally {
                lock.unlock()
            }
        }

        @JvmStatic
        fun isServiceRunning(context: Context): Boolean {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
                if (NodeService::class.java.name == service.service.className) {
                    return true
                }
            }
            return false
        }

        @JvmStatic
        fun stop(context: Context) {
            context.stopService(Intent(context, NodeService::class.java))
        }

        @JvmStatic
        fun start(context: Context) {
            context.startService(Intent(context, NodeService::class.java))
        }
    }
}