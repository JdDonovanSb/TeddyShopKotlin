package com.example.appinterface.Api.Usuarios

import Models.Usuarios.Usuario
import retrofit2.Call
import retrofit2.http.*

interface UsuarioApiService {
    @GET("usuario")
    fun listarUsuario(): Call<List<Usuario>>

    @POST("usuario")
    fun crearUsuario(@Body usuario: Usuario): Call <Void>

    @GET("usuario/{id}")
    fun obtenerUsuarioPorId(@Path("id") id: String): Call<Usuario>

    @PUT("usuario/{id}")
    fun actualizarUsuario(@Path("id") id: String, @Body usuario: Usuario): Call<Void>

    @DELETE("usuario/{id}")
    fun eliminarUsuario(@Path("id") id: String): Call<Void>
}