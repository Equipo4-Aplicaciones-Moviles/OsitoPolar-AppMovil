package com.example.ositopolarapp.features.subscriptions.data.mapper

import com.example.ositopolarapp.features.subscriptions.data.dto.PlanDto
import com.example.ositopolarapp.features.subscriptions.domain.model.BillingCycle
import com.example.ositopolarapp.features.subscriptions.domain.model.Plan

/**
 * Extension function to convert PlanDto to Plan domain entity.
 */
fun PlanDto.toEntity(): Plan {
    return Plan(
        id = id,
        planName = planName,
        price = price,
        billingCycle = when (billingCycle.lowercase()) {
            "monthly" -> BillingCycle.MONTHLY
            "yearly" -> BillingCycle.YEARLY
            else -> BillingCycle.MONTHLY
        },
        maxEquipment = maxEquipment,
        maxClients = maxClients,
        features = features
    )
}
