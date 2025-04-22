package com.example.appinterface.Api.Pedidos


import Models.Pedidos.Pedido;
import retrofit2.Call;
import retrofit2.http.*;

interface PedidoApiService {

    @POST("pedido")
    fun crearPedido(@Body pedido: Pedido): Call<Void>

    @GET("pedido")
    fun listarPedidos(): Call<List<Pedido>>

    @GET("pedido/{id}")
    fun obtenerPedidoPorId(@Path("id") id: String): Call<Pedido>

    @PUT("pedido/{id}")
    fun actualizarPedido(@Path("id") id: String, @Body pedido: Pedido): Call<Void>

    @DELETE("pedido/{id}")
    fun eliminarPedido(@Path("id") id: String): Call<Void>
}
