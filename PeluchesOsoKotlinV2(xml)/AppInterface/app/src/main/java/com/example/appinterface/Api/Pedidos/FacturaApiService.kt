package com.example.appinterface.Api.Pedidos

import Models.Pedidos.Factura
import Models.Productos.Catalogo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FacturaApiService {
    @POST("factura")
    fun crearFactura(@Body factura: Factura): Call<Void>

    @PUT("factura/{id}")
    fun actualizarFactura(@Path("id") id: String, @Body factura: Factura): Call<Void>

    @GET("factura/{id}")
    fun obtenerFacturaPorId(@Path("id") id: String): Call<Catalogo>

    @GET("factura")
    fun listarFacturas(): Call<List<Factura>>

    @DELETE("factura/{id}")
    fun eliminarFactura(@Path("id") id: String): Call<Void>
}
