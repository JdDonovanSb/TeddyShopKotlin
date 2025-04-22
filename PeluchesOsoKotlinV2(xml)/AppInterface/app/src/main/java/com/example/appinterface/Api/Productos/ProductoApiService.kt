package com.example.appinterface.Api.Productos


import Models.Productos.Producto
import retrofit2.Call;
import retrofit2.http.*;

interface ProductoApiService {

    @POST("producto")
    fun crearProducto(@Body producto: Producto): Call<Void>

    @GET("producto")
    fun listarProductos(): Call<List<Producto>>

    @GET("producto/{id}")
    fun obtenerProductoPorId(@Path("id") id: String): Call<Producto>

    @PUT("producto/{id}")
    fun actualizarProducto(@Path("id") id: String, @Body producto: Producto): Call<Void>

    @DELETE("producto/{id}")
    fun eliminarProducto(@Path("id") id: String): Call<Void>
}
