package com.example.ositopolarapp.features.notifications.data.repository

import android.util.Log
import com.example.ositopolarapp.features.notifications.data.api.NotificationApiService
import com.example.ositopolarapp.features.notifications.data.dto.NotificationDto
import com.example.ositopolarapp.features.notifications.domain.repository.NotificationRepository
import retrofit2.HttpException
import java.io.IOException

class NotificationRepositoryImpl(
    private val apiService: NotificationApiService
) : NotificationRepository {

    companion object {
        private const val TAG = "NotificationRepository"
    }

    override suspend fun getAllNotifications(): Result<List<NotificationDto>> {
        return try {
            val response = apiService.getAllNotifications()
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "Successfully fetched ${response.body()!!.size} notifications")
                Result.success(response.body()!!)
            } else {
                val errorMsg = "Failed to fetch notifications: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error while fetching notifications", e)
            Result.failure(Exception("Network error. Please check your connection."))
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error while fetching notifications: ${e.code()}", e)
            Result.failure(Exception("Server error: ${e.message()}"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error while fetching notifications", e)
            Result.failure(e)
        }
    }

    override suspend fun getUnreadCount(): Result<Int> {
        return try {
            val response = apiService.getUnreadCount()
            if (response.isSuccessful && response.body() != null) {
                val count = response.body()!!.count
                Log.d(TAG, "Unread notifications count: $count")
                Result.success(count)
            } else {
                val errorMsg = "Failed to fetch unread count: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error while fetching unread count", e)
            Result.failure(e)
        }
    }

    override suspend fun markAsRead(notificationId: Int): Result<Unit> {
        return try {
            val response = apiService.markAsRead(notificationId)
            if (response.isSuccessful) {
                Log.d(TAG, "Marked notification $notificationId as read")
                Result.success(Unit)
            } else {
                val errorMsg = "Failed to mark notification as read: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error while marking notification as read", e)
            Result.failure(e)
        }
    }

    override suspend fun markAllAsRead(): Result<Unit> {
        return try {
            val response = apiService.markAllAsRead()
            if (response.isSuccessful) {
                Log.d(TAG, "Marked all notifications as read")
                Result.success(Unit)
            } else {
                val errorMsg = "Failed to mark all as read: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error while marking all as read", e)
            Result.failure(e)
        }
    }
}
