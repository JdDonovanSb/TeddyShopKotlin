package Controller.Productos

import Models.Productos.Categoria
import Network.CategoriaApiService
import com.example.appinterface.Api.RetrofitClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class CategoriaController {

    private val api = RetrofitClient.instance

    fun listarCategorias(callback: (List<Categoria>?) -> Unit) {
        api.listarCategorias().enqueue(object : Callback<List<Categoria>> {
            override fun onResponse(call: Call<List<Categoria>>, response: Response<List<Categoria>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    println("Error en listarCategorias: ${response.errorBody()?.string()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Categoria>>, t: Throwable) {
                println("Error de red en listarCategorias: ${t.message}")
                callback(null)
            }
        })
    }

    fun crearCategoria(nombre: String, descripcion: String, imagen: String?, productos: List<String>?, callback: (Boolean) -> Unit) {
        val categoria = Categoria(id = null, nombre = nombre, descripcion = descripcion, imagen = imagen, productos = productos ?: emptyList())

        api.crearCategoria(categoria).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    println("Error en crearCategoria: ${response.errorBody()?.string()}")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Error de red en crearCategoria: ${t.message}")
                callback(false)
            }
        })
    }
}
