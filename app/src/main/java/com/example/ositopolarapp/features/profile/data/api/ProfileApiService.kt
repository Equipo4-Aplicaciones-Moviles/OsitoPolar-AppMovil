package com.example.ositopolarapp.features.profile.data.api

import com.example.ositopolarapp.features.profile.data.dto.CreateProfileRequest
import com.example.ositopolarapp.features.profile.data.dto.ProfileDto
import retrofit2.Response
import retrofit2.http.*

/**
 * Profile API Service
 *
 * Endpoints:
 * - GET /profiles/{id} - Get profile by ID
 * - POST /profiles - Create profile
 * - GET /profiles - Get all profiles
 */
interface ProfileApiService {

    /**
     * Get profile by ID
     * Endpoint: GET /api/v1/profiles/{profileId}
     */
    @GET("profiles/{profileId}")
    suspend fun getProfileById(
        @Path("profileId") profileId: Int
    ): Response<ProfileDto>

    /**
     * Create new profile
     * Endpoint: POST /api/v1/profiles
     */
    @POST("profiles")
    suspend fun createProfile(
        @Body request: CreateProfileRequest
    ): Response<ProfileDto>

    /**
     * Get all profiles
     * Endpoint: GET /api/v1/profiles
     */
    @GET("profiles")
    suspend fun getAllProfiles(): Response<List<ProfileDto>>
}
