package Controller.Productos

import Models.Productos.Catalogo
import Network.CatalogoApiService
import android.util.Log
import com.example.appinterface.Api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatalogoController {
    private val api = RetrofitClient.catalogoService

    fun crearCatalogo( nombre: String, descripcion: String, disponible: Boolean, estilo: String, imagen : String?, productos: List<String>?, companias: List<String>?,  callback: (Boolean) -> Unit) {
        val catalogo = Catalogo(
            id = null,
            nombreCatalogo = nombre,
            descripcionCatalogo = descripcion,
            disponibilidadCatalogo = disponible,
            estiloCatalogo = estilo,
            imagen = imagen,
            productos = productos ?: emptyList(),
            //companias = companias
        )

        api.crearCatalogo(catalogo).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun actualizarCatalogo(id: String, nombre: String, descripcion: String, disponible: Boolean, estilo: String, callback: (Boolean) -> Unit) {
        val catalogo = Catalogo(
            nombreCatalogo = nombre,
            descripcionCatalogo = descripcion,
            disponibilidadCatalogo = disponible,
            estiloCatalogo = estilo,
        )

        api.actualizarCatalogo(id, catalogo).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (!response.isSuccessful) {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ACTUALIZAR_ERROR", "Código: ${response.code()} - Error: $errorBody")
                }
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ACTUALIZAR_FAILURE", "Error de red: ${t.message}")
                callback(false)
            }
        })
    }

    fun listarCatalogos(callback: (List<Catalogo>?) -> Unit) {
        api.listarCatalogos().enqueue(object : Callback<List<Catalogo>> {
            override fun onResponse(call: Call<List<Catalogo>>, response: Response<List<Catalogo>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Catalogo>>, t: Throwable) {
                callback(null)
            }
        })
    }



    fun eliminarCatalogo(id: String, callback: (Boolean) -> Unit) {
        api.eliminarCatalogo(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }
}
