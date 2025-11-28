package com.example.ositopolarapp.features.subscriptions.data.mapper

import com.example.ositopolarapp.features.subscriptions.data.dto.PlanDto
import com.example.ositopolarapp.features.subscriptions.domain.model.Plan

fun PlanDto.toDomain(): Plan {
    return Plan(
        id = this.id,
        // CORRECCIÓN: Usamos 'name' (que viene del DTO) en lugar de 'planName'
        name = this.name,
        price = this.price,
        description = this.description,
        features = this.features,
        // CORRECCIÓN: Usamos 'maxEquipment' (que viene del DTO) en lugar de 'maxClients'
        maxEquipment = this.maxEquipment
    )
}