package hr.algebra.nasa

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import hr.algebra.nasa.api.NasaFetcher

private const val JOB_ID = 1

class NasaService : JobIntentService() {
    override fun onHandleWork(intent: Intent) {
        // fetch JSON
        NasaFetcher(this).fetchItems()

//        // put in DB
//        Thread.sleep(6000)
//
//        // send broadcast
//        sendBroadcast<NasaReceiver>()
    }

    companion object {
        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, NasaService::class.java, JOB_ID, intent)
        }
    }
}
