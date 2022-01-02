
package com.vikas.mobile.mynotes.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vikas.mobile.mynotes.data.MySafeNotesDatabase
import com.vikas.mobile.mynotes.data.entity.Setting
import kotlinx.coroutines.coroutineScope

class DefaultSettingsWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {

        val safenoteDb = MySafeNotesDatabase.getInstance(applicationContext)

        safenoteDb.settingsDao().insert(Setting(
            name = Setting.AUTH_SETTINGS_NAME, value = Setting.AUTH_OFF
        ))

        Result.success()
    }

}
