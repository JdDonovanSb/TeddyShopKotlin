package com.example.appinterface.Api.Productos

import Models.Productos.HistorialPrecio
import retrofit2.Call
import retrofit2.http.*

interface HistorialPrecioApiService {
    @GET("historialPrecio")
    fun listarHistorialPrecios(): Call<List<HistorialPrecio>>

    @POST("historialPrecio")
    fun crearHistorialPrecio(@Body historialPrecio: HistorialPrecio): Call <Void>

    @GET("historialPrecio/{id}")
    fun obtenerHistorialPrecioPorId(@Path("id") id: String): Call<HistorialPrecio>

    @PUT("historialPrecio/{id}")
    fun actualizarHistorialPrecio(@Path("id") id: String, @Body historialPrecio: HistorialPrecio): Call<Void>

    @DELETE("historialPrecio/{id}")
    fun eliminarHistorialPrecio(@Path("id") id: String): Call<Void>
}