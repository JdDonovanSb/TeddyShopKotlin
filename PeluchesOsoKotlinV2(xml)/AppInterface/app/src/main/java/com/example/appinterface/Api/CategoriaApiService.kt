package com.example.appinterface.Api

import Models.Productos.Categoria
import retrofit2.Call
import retrofit2.http.*

interface CategoriaApiService {
    @GET("categorias")  // Ruta correcta basada en la API Express
    fun listarCategorias(): Call<List<Categoria>>

    @POST("categorias")
    fun crearCategoria(@Body categoria: Categoria): Call<Void>

    @GET("categorias/{id}")
    fun obtenerCategoriaPorId(@Path("id") id: String): Call<Categoria>

    @PUT("categorias/{id}")
    fun actualizarCategoria(@Path("id") id: String, @Body categoria: Categoria): Call<Void>

    @DELETE("categorias/{id}")
    fun eliminarCategoria(@Path("id") id: String): Call<Void>
}
