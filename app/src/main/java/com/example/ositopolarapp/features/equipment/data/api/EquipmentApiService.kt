package com.example.ositopolarapp.features.equipment.data.api

import com.example.ositopolarapp.features.equipment.data.dto.CreateEquipmentRequest
import com.example.ositopolarapp.features.equipment.data.dto.EquipmentDto
import com.example.ositopolarapp.features.equipment.data.dto.UpdateOperationsRequest
import retrofit2.Response
import retrofit2.http.*

interface EquipmentApiService {

    @GET("equipment") // O "equipments" según tu backend
    suspend fun getAllEquipments(): Response<List<EquipmentDto>>

    @GET("equipment/{id}")
    suspend fun getEquipmentById(@Path("id") id: Int): Response<EquipmentDto>

    @POST("equipment")
    suspend fun createEquipment(@Body request: CreateEquipmentRequest): Response<EquipmentDto>

    @PUT("equipment/{id}/operations") // Ajusta la ruta si es distinta
    suspend fun updateOperations(
        @Path("id") id: Int,
        @Body request: UpdateOperationsRequest
    ): Response<EquipmentDto>

    @DELETE("equipment/{id}")
    suspend fun deleteEquipment(@Path("id") id: Int): Response<Void>
}