package com.example.shopmobileapplication.data.notifications

import com.example.shopmobileapplication.data.Notification

interface NotificationsRepository {
    suspend fun getAllNotificationByUserId(userId: String): Result<List<Notification>>
    suspend fun updateNotification(notification: Notification): Result<Boolean>
}