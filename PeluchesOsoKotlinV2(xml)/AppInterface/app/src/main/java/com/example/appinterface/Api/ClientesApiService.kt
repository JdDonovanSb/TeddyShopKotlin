package com.example.appinterface.Api.Usuarios

import Models.Usuarios.Clientes
import retrofit2.Call
import retrofit2.http.*

interface ClientesApiService {

    // 🔍 Obtener todos los clientes
    @GET("clientes")
    fun listarClientes(): Call<List<Clientes>>

    // 🔍 Buscar cliente por DNI
    @GET("clientes/{dni}")
    fun obtenerClientePorId(@Path("dni") dni: String): Call<Clientes>

    // ➕ Crear nuevo cliente
    @POST("clientes")
    fun crearCliente(@Body cliente: Clientes): Call<Void>

    // ✏️ Actualizar cliente existente
    @PUT("clientes/{id}")
    fun actualizarCliente(@Path("id") id: String, @Body cliente: Clientes): Call<Void>

    // ❌ Eliminar cliente por DNI
    @DELETE("clientes/{dni}")
    fun eliminarCliente(@Path("dni") dni: String): Call<Void>
}
