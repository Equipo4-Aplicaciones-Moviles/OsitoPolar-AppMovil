package com.example.ositopolarapp.features.equipment.domain.usecase

import com.example.ositopolarapp.features.equipment.domain.model.Equipment
import com.example.ositopolarapp.features.equipment.domain.repository.EquipmentRepository

class GetAllEquipmentsUseCase(
    private val repository: EquipmentRepository
) {
    suspend operator fun invoke(): Result<List<Equipment>> {
        // CORRECCIÓN: Agregamos la 's' al final para que coincida con el Repositorio
        return repository.getAllEquipments()
    }
}