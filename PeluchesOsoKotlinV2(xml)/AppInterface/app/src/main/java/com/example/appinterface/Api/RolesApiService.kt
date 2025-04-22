package com.example.appinterface.Api.Usuarios

import Models.Pedidos.Devoluciones
import Models.Usuarios.Roles
import retrofit2.Call
import retrofit2.http.*

interface RolesApiService {

    @GET("roles")
    fun listarRoles(): Call<List<Roles>>

    @POST("roles")
    fun crearRoles(@Body roles: Roles): Call<   Void>

    @PUT("roles/{id}")
    fun actualizarRoles(@Path("id") id: String, @Body roles: Roles): Call<Void>

    @DELETE("roles/{id}")
    fun eliminarRoles(@Path("id") id: String): Call<Void>

}