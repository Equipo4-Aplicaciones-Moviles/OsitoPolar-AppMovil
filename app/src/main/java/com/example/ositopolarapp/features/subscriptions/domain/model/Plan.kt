package com.example.ositopolarapp.features.subscriptions.domain.model

/**
 * Plan (Subscription) domain entity.
 * Represents a subscription plan for Owners or Providers.
 */
data class Plan(
    val id: Int = 0,
    val planName: String = "",
    val price: Double = 0.0,
    val billingCycle: BillingCycle = BillingCycle.MONTHLY,
    val maxEquipment: Int? = null,      // For Owners
    val maxClients: Int? = null,        // For Providers
    val features: List<String> = emptyList()
) {
    /**
     * Returns a formatted price string with billing cycle.
     */
    fun getFormattedPrice(): String {
        return "$${"%.2f".format(price)} / ${billingCycle.displayName}"
    }

    /**
     * Checks if this plan is for Owners (has maxEquipment).
     */
    fun isOwnerPlan(): Boolean = maxEquipment != null

    /**
     * Checks if this plan is for Providers (has maxClients).
     */
    fun isProviderPlan(): Boolean = maxClients != null

    /**
     * Gets the plan type as a string.
     */
    fun getPlanType(): String = when {
        isOwnerPlan() -> "Owner"
        isProviderPlan() -> "Provider"
        else -> "Unknown"
    }

    /**
     * Gets usage limit display string.
     */
    fun getUsageLimitDisplay(): String {
        return when {
            maxEquipment != null -> "Hasta $maxEquipment equipos"
            maxClients != null -> if (maxClients == -1) "Clientes ilimitados" else "Hasta $maxClients clientes"
            else -> "Sin límite especificado"
        }
    }
}

/**
 * Billing cycle enum.
 */
enum class BillingCycle(val displayName: String) {
    MONTHLY("mes"),
    YEARLY("año")
}
