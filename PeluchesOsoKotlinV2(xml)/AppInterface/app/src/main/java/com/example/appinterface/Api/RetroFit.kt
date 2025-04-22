package com.example.appinterface.Api

import Network.CategoriaApiService
import Network.CatalogoApiService
import Network.CompaniaApiService
import com.example.appinterface.Api.Pedidos.*
import com.example.appinterface.Api.Productos.*
import com.example.appinterface.Api.Usuarios.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000/api/"
    private const val MAGS_URL = "https://vigilant-space-meme-7vvpgv5g5vgwfrw7x-3000.app.github.dev/api/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Muestra todo el cuerpo de las solicitudes y respuestas
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)  // Agrega el OkHttpClient con el interceptor
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

    val devolucionesService: DevolucionesApiService
        get() = retrofit.create(DevolucionesApiService::class.java)

    val rolesService: RolesApiService
        get() = retrofit.create(RolesApiService::class.java)

    val pedidoService: PedidoApiService
        get() = retrofit.create(PedidoApiService::class.java)

    val historialPrecioService: HistorialPrecioApiService
        get() = retrofit.create(HistorialPrecioApiService::class.java)

    val productoService: ProductoApiService
        get() = retrofit.create(ProductoApiService::class.java)

    val companiaService: CompaniaApiService
        get() = retrofit.create(CompaniaApiService::class.java)
}



