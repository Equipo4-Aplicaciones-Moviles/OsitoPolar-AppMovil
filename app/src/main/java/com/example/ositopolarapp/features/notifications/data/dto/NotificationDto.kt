package com.example.ositopolarapp.features.notifications.data.dto

/**
 * Notification data transfer object
 */
data class NotificationDto(
    val id: Int,
    val title: String,
    val message: String,
    val type: String,  // "PAYMENT", "RENTAL", "SERVICE", "SYSTEM", "MAINTENANCE"
    val isRead: Boolean,
    val createdAt: String
)
