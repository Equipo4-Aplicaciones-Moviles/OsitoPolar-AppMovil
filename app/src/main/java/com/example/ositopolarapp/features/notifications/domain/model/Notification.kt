package com.example.ositopolarapp.features.notifications.domain.model

/**
 * Notification Domain Model
 *
 * Represents a user notification.
 * NOTE: Backend endpoints not yet implemented - using mock data.
 */
data class Notification(
    val id: Int,
    val title: String,
    val message: String,
    val type: NotificationType,
    val isRead: Boolean,
    val createdAt: String
) {
    fun getTypeIcon(): String {
        return when (type) {
            NotificationType.PAYMENT -> "💳"
            NotificationType.RENTAL -> "🧊"
            NotificationType.SERVICE -> "🔧"
            NotificationType.ALERT -> "⚠️"
            NotificationType.INFO -> "ℹ️"
        }
    }
}

enum class NotificationType {
    PAYMENT,
    RENTAL,
    SERVICE,
    ALERT,
    INFO
}
