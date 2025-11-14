package com.example.ositopolarapp.features.notifications.domain.repository

import com.example.ositopolarapp.features.notifications.data.dto.NotificationDto

interface NotificationRepository {
    suspend fun getAllNotifications(): Result<List<NotificationDto>>
    suspend fun getUnreadCount(): Result<Int>
    suspend fun markAsRead(notificationId: Int): Result<Unit>
    suspend fun markAllAsRead(): Result<Unit>
}
