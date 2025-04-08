package com.example.appinterface.Api

import Network.CategoriaApiService
import Network.CatalogoApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000/api/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val categoriaService: CategoriaApiService
        get() = retrofit.create(CategoriaApiService::class.java)

    val catalogoService: CatalogoApiService
        get() = retrofit.create(CatalogoApiService::class.java)

    val devolucionesService: DevolucionesApiService
        get() = retrofit.create(DevolucionesApiService::class.java)

    val rolesService: RolesApiService
        get() = retrofit.create(RolesApiService::class.java)
}


