package com.example.shopmobileapplication.data.notifications

import com.example.shopmobileapplication.data.Notification
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

class NotificationsRepositoryImpl(
    private val supabaseClient: SupabaseClient
): NotificationsRepository {
    override suspend fun getAllNotificationByUserId(userId: String): Result<List<Notification>> = try {
        Result.success(supabaseClient.postgrest[Notification.tableName].select(filter = {
            Notification::userId eq userId
        }).decodeList<Notification>())
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateNotification(notification: Notification): Result<Boolean> = try {
        supabaseClient.postgrest[Notification.tableName].update(update = {
            Notification::readAt setTo notification.readAt
        }, filter = {
            Notification::id eq notification.id
        })
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }
}