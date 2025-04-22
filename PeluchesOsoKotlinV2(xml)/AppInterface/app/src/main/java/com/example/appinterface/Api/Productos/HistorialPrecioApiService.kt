package com.example.appinterface.Api.Productos

import Models.Productos.HistorialPrecio
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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