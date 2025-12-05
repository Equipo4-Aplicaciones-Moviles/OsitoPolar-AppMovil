package com.example.ositopolarapp.features.notifications.data.api

import com.example.ositopolarapp.features.notifications.data.dto.NotificationDto
import com.example.ositopolarapp.features.notifications.data.dto.UnreadCountResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Notification API Service
 *
 * Endpoints:
 * - GET /notifications - Get user notifications
 * - GET /notifications/unread-count - Get unread count
 * - PATCH /notifications/{id}/read - Mark as read
 * - PATCH /notifications/read-all - Mark all as read
 */
interface NotificationApiService {

    /**
     * Get all notifications for current user
     * Endpoint: GET /api/v1/notifications
     */
    @GET("notifications")
    suspend fun getAllNotifications(): Response<List<NotificationDto>>

    /**
     * Get unread notification count
     * Endpoint: GET /api/v1/notifications/unread-count
     */
    @GET("notifications/unread-count")
    suspend fun getUnreadCount(): Response<UnreadCountResponse>

    /**
     * Mark a notification as read
     * Endpoint: PATCH /api/v1/notifications/{id}/read
     */
    @PATCH("notifications/{id}/read")
    suspend fun markAsRead(
        @Path("id") notificationId: Int
    ): Response<Unit>

    /**
     * Mark all notifications as read
     * Endpoint: PATCH /api/v1/notifications/read-all
     */
    @PATCH("notifications/read-all")
    suspend fun markAllAsRead(): Response<Unit>
}
