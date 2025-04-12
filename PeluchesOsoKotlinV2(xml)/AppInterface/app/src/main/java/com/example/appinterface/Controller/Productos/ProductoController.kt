package Controllers.Productos

import Models.Productos.Producto
import com.example.appinterface.Api.Productos.ProductoApiService
import com.example.appinterface.Api.RetrofitClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class ProductoController {

    private val api = RetrofitClient.productoService

    // Crear un producto
    fun crearProducto(
        estiloProducto: String,
        materialProducto: String,
        tamañoProducto: String,
        disponibilidadProducto: String,
        imagen: String?,
        historialPrecios: List<String>?,
        catalogos: List<String>?,
        categorias: List<String>?,
        callback: (Boolean) -> Unit
    ) {
        val producto = Producto(
            id = null,
            estiloProducto = estiloProducto,
            materialProducto = materialProducto,
            tamañoProducto = tamañoProducto,
            disponibilidadProducto = disponibilidadProducto,
            imagen = imagen,
            historialPrecios = historialPrecios ?: emptyList(),
           // catalogos = catalogos ?: emptyList(),
          //  categorias = categorias ?: emptyList()
        )

        api.crearProducto(producto).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    println("Error al crear producto: ${response.errorBody()?.string()}")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Error de red al crear producto: ${t.message}")
                callback(false)
            }
        })
    }

    // Listar todos los productos
    fun listarProductos(callback: (List<Producto>?) -> Unit) {
        api.listarProductos().enqueue(object : Callback<List<Producto>> {
            override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    println("Error al listar productos: ${response.errorBody()?.string()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                println("Error de red al listar productos: ${t.message}")
                callback(null)
            }
        })
    }

    // Actualizar un producto por ID
    fun actualizarProducto(
        id: String,
        estiloProducto: String,
        materialProducto: String,
        tamañoProducto: String,
        disponibilidadProducto: String,
        imagen: String?,
        historialPrecios: List<String>?,
        catalogos: List<String>?,
        categorias: List<String>?,
        callback: (Boolean) -> Unit
    ) {
        val producto = Producto(
            id = id, // Asumiendo que el modelo Producto incluye el id
            estiloProducto = estiloProducto,
            materialProducto = materialProducto,
            tamañoProducto = tamañoProducto,
            disponibilidadProducto = disponibilidadProducto,
            imagen = imagen,
            historialPrecios = historialPrecios ?: emptyList(),
            //catalogos = catalogos ?: emptyList(),
           // categorias = categorias ?: emptyList()
        )

        api.actualizarProducto(id, producto).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }

    // Eliminar un prozducto
    fun eliminarProducto(id: String, callback: (Boolean) -> Unit) {
        api.eliminarProducto(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }
}
