package Network

import Models.Productos.Catalogo
import retrofit2.Call
import retrofit2.http.*

interface CatalogoApiService {
    @GET("catalogos")  // Ruta correcta basada en la API Express
    fun listarCatalogos(): Call<List<Catalogo>>

    @POST("categorias")
    fun crearCatalogo(@Body catalogo: Catalogo): Call<Void>

    @GET("categorias/{id}")
    fun obtenerCatalogoPorId(@Path("id") id: String): Call<Catalogo>

    @PUT("catalogos/{id}")
    fun actualizarCatalogo(@Path("id") id: String, @Body catalogo: Catalogo): Call<Void>

    @DELETE("catalogos}")
    fun eliminarCatalogo(@Path("id") id: String): Call<Void>
}