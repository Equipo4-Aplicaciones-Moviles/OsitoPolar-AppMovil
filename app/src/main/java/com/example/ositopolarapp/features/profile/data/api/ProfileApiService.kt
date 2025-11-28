package com.example.ositopolarapp.features.profile.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
// IMPORTANTE: Estos son los paquetes correctos según tu estructura (image_94883c.jpg)
import com.example.ositopolarapp.features.profile.data.dto.ProfileDto
import com.example.ositopolarapp.features.profile.data.dto.UpdateProfileRequest

interface ProfileApiService {

    @GET("profiles/{id}")
    suspend fun getProfile(
        @Path("id") profileId: Int
    ): Response<ProfileDto>

    @PUT("profiles/{id}")
    suspend fun updateProfile(
        @Path("id") profileId: Int,
        @Body request: UpdateProfileRequest
    ): Response<ProfileDto>
}