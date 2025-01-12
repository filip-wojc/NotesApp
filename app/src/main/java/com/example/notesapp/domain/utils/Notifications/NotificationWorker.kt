package com.example.notesapp.domain.utils.Notifications

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(context:Context, params: WorkerParameters): Worker(context, params) {
    override fun doWork(): Result{
        val title = inputData.getString("title") ?: "Reminder"
        val message = inputData.getString("message") ?: "Time for your reminder!"

        NotificationsUtility.showNotification(applicationContext, title, message)

        return Result.success()
    }
}