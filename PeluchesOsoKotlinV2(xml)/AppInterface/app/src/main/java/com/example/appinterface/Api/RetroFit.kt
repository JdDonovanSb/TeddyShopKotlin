package com.example.appinterface.Api

import Models.Pedidos.MetodoPago
import Network.CategoriaApiService
import Network.CatalogoApiService
import Network.ClientesApiService
import Network.MetodoPagoApiService
import Network.MovimientoApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
   private const val BASE_URL = "http://10.0.2.2:3000/api/"
    private const val MAGS_URL = "https://vigilant-space-meme-7vvpgv5g5vgwfrw7x-3000.app.github.dev/api/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(MAGS_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val categoriaService: CategoriaApiService
        get() = retrofit.create(CategoriaApiService::class.java)

    val catalogoService: CatalogoApiService
        get() = retrofit.create(CatalogoApiService::class.java)

    val clientesService: ClientesApiService
        get() =retrofit.create(ClientesApiService::class.java)

    val movimientoService: MovimientoApiService
        get() =retrofit.create(MovimientoApiService::class.java)

    val MetodoPagoService: MetodoPagoApiService
        get() =retrofit.create(MetodoPagoApiService::class.java)
}


