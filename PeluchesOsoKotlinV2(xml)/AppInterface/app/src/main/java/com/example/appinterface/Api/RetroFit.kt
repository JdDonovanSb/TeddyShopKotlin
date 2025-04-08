package com.example.appinterface.Api

import Network.CategoriaApiService
import Network.CatalogoApiService
import com.example.appinterface.Api.Pedidos.DetalleFacturaApiService
import com.example.appinterface.Api.Usuarios.EmpleadoApiService
import com.example.appinterface.Api.Usuarios.UsuarioApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000/api/"
    private const val MAGS_URL = "https://vigilant-space-meme-7vvpgv5g5vgwfrw7x-3000.app.github.dev/api/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val categoriaService: CategoriaApiService
        get() = retrofit.create(CategoriaApiService::class.java)

    val catalogoService: CatalogoApiService
        get() = retrofit.create(CatalogoApiService::class.java)

    val usuarioService: UsuarioApiService
        get() = retrofit.create(UsuarioApiService::class.java)

    val detalleFacturaService: DetalleFacturaApiService
        get() = retrofit.create(DetalleFacturaApiService::class.java)

    val empleadoService: EmpleadoApiService
        get() = retrofit.create(EmpleadoApiService::class.java)


}
