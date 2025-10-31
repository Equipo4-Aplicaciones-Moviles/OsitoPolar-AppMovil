package com.example.ositopolarapp.core.data.api

import com.example.ositopolarapp.core.domain.models.LoginRequestDto
import com.example.ositopolarapp.core.domain.models.LoginResponseDto
import com.example.ositopolarapp.core.domain.models.RegisterRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    // Coincide con: POST /api/v1/authentication/sign-in
    @POST("api/v1/authentication/sign-in")
    suspend fun signIn(@Body request: LoginRequestDto): LoginResponseDto

    // Coincide con: POST /api/v1/authentication/sign-up
    // Nota: Esta API devuelve un 200 OK sin un cuerpo de respuesta,
    // por lo que el tipo de retorno en Retrofit puede ser 'Unit' o 'Response<Unit>'.
    @POST("api/v1/authentication/sign-up")
    suspend fun signUp(@Body request: RegisterRequestDto) // o: suspend fun signUp(...): Response<Unit>

}