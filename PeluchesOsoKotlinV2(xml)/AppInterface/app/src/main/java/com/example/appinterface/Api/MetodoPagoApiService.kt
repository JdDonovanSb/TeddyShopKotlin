package com.example.appinterface.Api.Usuarios
import Models.Pedidos.MetodoPago
import retrofit2.Call;
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MetodoPagoApiService {

    @GET("metodoPago")
    fun listarMetodoPago(): Call<List<MetodoPago>>

    @POST("metodoPago")
    fun crearMetodoPago(@Body metodoPago: MetodoPago): Call<Void>

    @PUT("metodoPago/{id}")
    fun actualizarMetodoPago(
        @Path("id") id: String,
        @Body metodoPago: MetodoPago
    ): Call<Void>

    @DELETE("metodoPago/{id}")
    fun eliminarMetodoPago(@Path("id") id: String): Call<Void>
}
