package Network

import Models.Productos.Catalogo
import retrofit2.Call
import retrofit2.http.*

interface CatalogoApiService {
    @GET("catalogos/activos")
    fun listarCatalogos(): Call<List<Catalogo>>

    @POST("catalogos")
    fun crearCatalogo(@Body catalogo: Catalogo): Call<Void>

    @GET("catalogos/{id}")
    fun obtenerCatalogoPorId(@Path("id") id: String): Call<Catalogo>

    @PUT("catalogos/{id}")
    fun actualizarCatalogo(@Path("id") id: String, @Body catalogo: Catalogo): Call<Void>

    @PATCH("catalogos/{id}/desactivar")
    fun eliminarCatalogo(@Path("id") id: String): Call<Void>
}