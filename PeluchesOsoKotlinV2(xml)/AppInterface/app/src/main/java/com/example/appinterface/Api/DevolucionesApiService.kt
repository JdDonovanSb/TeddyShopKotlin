package com.example.appinterface.Api

import Models.Pedidos.Devoluciones
import retrofit2.Call
import retrofit2.http.*

interface DevolucionesApiService {

    @GET("devoluciones")
    fun listartDevoluciones(): Call<List<Devoluciones>>

    @POST("devoluciones")
    fun crearDevoluciones(@Body devoluciones: Devoluciones): Call<Void>

    @PUT("devoluciones/{id}")
    fun actualizarDevoluciones(@Path("id") id: String, @Body devoluciones: Devoluciones): Call<Void>

    @DELETE("devoluciones/{id}")
    fun eliminarDevoluciones(@Path("id") id: String): Call<Void>
}