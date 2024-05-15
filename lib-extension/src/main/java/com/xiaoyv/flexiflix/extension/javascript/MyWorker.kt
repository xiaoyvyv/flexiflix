import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class MyWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                // 模拟网络请求
                makeNetworkRequest()

                // 网络请求成功
                Result.success()
            } catch (e: Exception) {
                // 网络请求失败
                Result.failure()
            }
        }
    }

    private suspend fun makeNetworkRequest() {
        // 模拟网络请求
        delay(3000) // 假设网络请求需要3秒钟
        println("Network request completed")
    }
}
