package com.example.appinterface.Api

import com.example.appinterface.Models.Login.LoginRequest
import com.example.appinterface.Models.Login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}