package com.example.appinterface.Models.Login

data class LoginRequest(
    val email: String,
    val contraseña: String // Mantenemos el nombre exacto que espera el backend
)