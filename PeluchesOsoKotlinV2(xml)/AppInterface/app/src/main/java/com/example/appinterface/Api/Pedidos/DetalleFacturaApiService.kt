package com.example.appinterface.Api.Pedidos

import Models.Pedidos.DetalleFactura
import retrofit2.Call
import retrofit2.http.*

interface DetalleFacturaApiService {

    @GET("detallesFactura")
    fun listarDetallesFactura(): Call<List<DetalleFactura>>

    @POST("detallesFactura")
    fun crearDetalleFactura(@Body detalleFactura: DetalleFactura): Call<Void>

    @GET("detallesFactura/{id}")
    fun obtenerDetalleFacturaPorId(@Path("id") id: String): Call<DetalleFactura>

    @PUT("detallesFactura/{id}")
    fun actualizarDetalleFactura(@Path("id") id: String, @Body detalleFactura: DetalleFactura): Call<Void>

    @DELETE("detallesFactura/{id}")
    fun eliminarDetalleFactura(@Path("id") id: String): Call<Void>
}
