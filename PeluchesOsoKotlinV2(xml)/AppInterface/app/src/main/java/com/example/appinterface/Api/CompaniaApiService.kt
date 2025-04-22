package com.example.appinterface.Api.Usuarios

import Models.Compania
import retrofit2.Call
import retrofit2.http.*

interface CompaniaApiService {

    @GET("compania")
    fun listarCompañias(): Call<List<Compania>>

    @POST("compania")
    fun crearCompañia(@Body compania: Compania): Call<Void>

    @GET("compania/{id}")
    fun obtenerCompañiaPorId(@Path("id") id: String): Call<Compania>

    @PUT("compania/{id}")
    fun actualizarCompañia(@Path("id") id: String, @Body compania: Compania): Call<Void>

    @DELETE("compania/{id}")
    fun eliminarCompañia(@Path("id") id: String): Call<Void>
}
