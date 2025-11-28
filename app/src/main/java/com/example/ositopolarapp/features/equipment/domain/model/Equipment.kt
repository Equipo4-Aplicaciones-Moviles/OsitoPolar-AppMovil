package com.example.ositopolarapp.features.equipment.domain.model

data class Equipment(
    val id: Int,
    val name: String,
    val type: String, // Usamos String en lugar de Enum para evitar errores de conversión
    val model: String,
    val serialNumber: String,
    val brand: String, // Antes era manufacturer
    val location: String, // Antes era locationName
    val address: String, // Antes era locationAddress
    val latitude: Double,
    val longitude: Double,
    val status: String, // Usamos String en lugar de Enum
    val temperature: Double, // Antes era currentTemperature
    val ownerId: Int,
    val imageUrl: String? = null,
    val energyConsumption: Double = 0.0,
    val isPoweredOn: Boolean = false
)