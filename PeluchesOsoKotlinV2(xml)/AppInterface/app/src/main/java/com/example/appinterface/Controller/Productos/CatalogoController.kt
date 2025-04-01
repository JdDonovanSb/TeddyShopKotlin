package Controller.Productos

import Models.Productos.Catalogo
import Network.CatalogoApiService
import com.example.appinterface.Api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatalogoController {
    private val api = RetrofitClient.catalogoInstance

    fun crearCatalogo(nombre: String, descripcion: String, disponible: Boolean, estilo: String, callback: (Boolean) -> Unit) {
        val catalogo = Catalogo(
            nombre = nombre,
            descripcion = descripcion,
            disponible = disponible,
            estilo = estilo
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

    fun actualizarCatalogo(id: String, nombre: String, descripcion: String, disponible: Boolean, estilo: String, callback: (Boolean) -> Unit) {
        val catalogo = Catalogo(
            nombre = nombre,
            descripcion = descripcion,
            disponible = disponible,
            estilo = estilo
        )

        api.actualizarCatalogo(id, catalogo).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
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