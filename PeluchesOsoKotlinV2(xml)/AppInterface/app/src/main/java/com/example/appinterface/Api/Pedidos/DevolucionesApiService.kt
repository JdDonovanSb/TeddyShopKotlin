package com.example.appinterface.Api.Pedidos

import Models.Pedidos.Devoluciones
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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