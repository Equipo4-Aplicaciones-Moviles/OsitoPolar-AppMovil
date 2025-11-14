package com.example.ositopolarapp.core.config

/**
 * API Configuration
 *
 * Centralized configuration for API endpoints and keys.
 *
 * Network Configuration:
 * - Android Emulator: Use 10.0.2.2 to access host machine's localhost
 * - Physical Device: Use your machine's IP address (e.g., 192.168.1.100)
 * - Production: Use actual server URL
 */
object ApiConfig {

    /**
     * Base URL for the API
     *
     * Development options:
     * - Emulator: "http://10.0.2.2:8080/api/v1/"
     * - Physical device on same network: "http://192.168.1.XXX:8080/api/v1/"
     * - Production: "https://api.ositopolar.com/api/v1/"
     * - Azure Deployed: "https://ositopolar-api.grayground-d49718c1.eastus.azurecontainerapps.io/api/v1/"
     */
    const val BASE_URL = "https://ositopolar-api.grayground-d49718c1.eastus.azurecontainerapps.io/api/v1/"

    /**
     * Admin Key for administrative operations
     *
     * WARNING: In production, this should NEVER be hardcoded.
     * Use environment variables or secure storage.
     */
    const val ADMIN_KEY = "3xV9jK7s5L+8b2Qw4FdYzP0WQf8N6YhJd7M1pR2sT0U="

    /**
     * API Endpoint Paths
     */
    object Endpoints {
        // Authentication
        const val AUTH = "auth/"
        const val LOGIN = "auth/login"
        const val REGISTER_CHECKOUT = "auth/registration/checkout"
        const val COMPLETE_REGISTRATION = "auth/registration/complete"
        const val VERIFY_2FA = "auth/verify-2fa"

        // Equipment
        const val EQUIPMENT = "equipments/"
        const val EQUIPMENT_BY_ID = "equipments/{id}"
        const val EQUIPMENT_OPERATIONS = "equipments/{id}/operations"

        // Service Requests
        const val SERVICE_REQUESTS = "serviceRequests/"
        const val SERVICE_REQUEST_BY_ID = "serviceRequests/{id}"
        const val SERVICE_REQUEST_FEEDBACK = "serviceRequests/{id}/feedback"
        const val SERVICE_REQUEST_STATUS = "serviceRequests/{id}/status"

        // Subscriptions
        const val PLANS = "plans/"
        const val PLAN_BY_ID = "plans/{id}"
    }

    /**
     * Network Timeouts (in seconds)
     */
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L
}
