package com.example.appinterface.Api.Usuarios


import Models.Usuarios.Empleados;
import retrofit2.Call;
import retrofit2.http.*;

interface EmpleadoApiService {

    @POST("empleado")
    fun crearEmpleado(@Body empleado: Empleados): Call<Void>

    @GET("empleado")
    fun listarEmpleados(): Call<List<Empleados>>

    @GET("empleado/{id}")
    fun obtenerEmpleadoPorId(@Path("id") id: String): Call<Empleados>

    @PUT("empleado/{id}")
    fun actualizarEmpleado(@Path("id") id: String, @Body empleado: Empleados): Call<Void>

    @DELETE("empleado/{id}")
    fun eliminarEmpleado(@Path("id") id: String): Call<Void>
}
