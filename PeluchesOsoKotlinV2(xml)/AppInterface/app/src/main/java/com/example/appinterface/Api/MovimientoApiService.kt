package com.example.appinterface.Api

import Models.Productos.Movimiento
import retrofit2.Call
import retrofit2.http.*

interface MovimientoApiService {

    @GET("movimiento")
    fun listarMovimientos(): Call<List<Movimiento>>

    @POST("movimiento")
    fun crearMovimiento(@Body movimiento: Movimiento): Call<Void>

    @PUT("movimiento/{id}")
    fun actualizarMovimiento(@Path("id") id: String, @Body movimiento: Movimiento): Call<Void>

    @DELETE("movimiento/{id}")
    fun eliminarMovimiento(@Path("id") id: String): Call<Void>
}
