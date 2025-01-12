package com.example.notesapp.domain.utils.Notifications

import android.content.Context
import android.os.Message
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object ReminderScheduler {
    fun scheduleReminder(context:Context, reminderTime: Long, title: String, message: String ){
        val currentTime = System.currentTimeMillis()
        val delay = reminderTime - currentTime

        if(delay > 0){
            val inputData = Data.Builder()
                .putString("title", title)
                .putString("message", message)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }

    }


}