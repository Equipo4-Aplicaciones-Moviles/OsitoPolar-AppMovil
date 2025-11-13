package com.example.ositopolarapp.features.authentication.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Lo que ENVIAMOS a: POST /api/v1/authentication/sign-in
 */
data class SignInRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)