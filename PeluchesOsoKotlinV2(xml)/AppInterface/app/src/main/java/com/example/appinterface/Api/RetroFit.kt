package com.example.appinterface.Api

import com.example.appinterface.Api.Pedidos.*
import com.example.appinterface.Api.Usuarios.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000/api/"
    private const val MAGS_URL = "https://laughing-space-zebra-wrrgwr5w5vx5h94wr-3000.app.github.dev/api/"

    // Configuración del interceptor para logging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Configuración del cliente HTTP
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(MAGS_URL) // Puedes cambiar a MAGS_URL cuando lo necesites
        .client(okHttpClient)
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

    val facturaService: FacturaApiService
        get() = retrofit.create(FacturaApiService::class.java)

    val detallePedidoService: DetallePedidoApiService
        get() = retrofit.create(DetallePedidoApiService::class.java)

    val authService: AuthApi
        get() = retrofit.create(AuthApi::class.java)
}



