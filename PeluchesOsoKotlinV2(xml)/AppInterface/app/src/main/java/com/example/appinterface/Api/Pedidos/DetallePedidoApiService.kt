package com.example.appinterface.Api.Pedidos

import Models.Pedidos.DetallePedido
import Models.Pedidos.Pedido
import Models.Productos.Catalogo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DetallePedidoApiService {
    @GET("detallesPedido")
    fun listarDetallesPedido(): Call<List<DetallePedido>>

    @POST("detallesPedido")
    fun crearDetallePedido(@Body detallePedido: DetallePedido): Call<Void>

    @PUT("detallesPedido/{id}")
    fun actualizarDetallePedido(@Path("id") id: String, @Body pedido: DetallePedido): Call<Void>

    @GET("detallesPedido/{id}")
    fun obtenerdetallePedidoPorId(@Path("id") id: String): Call<DetallePedido>


    @DELETE("detallesPedido/{id}")
    fun eliminarDetallePedido(@Path("id") id: String): Call<Void>
}