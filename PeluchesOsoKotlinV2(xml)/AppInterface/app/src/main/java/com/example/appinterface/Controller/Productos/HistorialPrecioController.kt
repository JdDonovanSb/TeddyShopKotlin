package Controller.Productos

import Models.Productos.HistorialPrecio
import com.example.appinterface.Api.Productos.HistorialPrecioApiService
import com.example.appinterface.Api.RetrofitClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Callback
import retrofit2.Response
import java.util.Date


class HistorialPrecioController {

    private val api = RetrofitClient.historialPrecioService

    // Crear un historial de precio
    fun crearHistorialPrecio(precio: Double, fechaInicio: Date, fechaFin: Date, estadoPrecio: Boolean,
                             producto: List<String>?, callback: (Boolean) -> Unit) {

        val historialPrecio = HistorialPrecio(
            id = null,
            precio = precio,
            fechaInicio = fechaInicio,
            fechaFin = fechaFin,
            estadoPrecio = estadoPrecio,
            //producto = producto ?: emptyList()
        )

        api.crearHistorialPrecio(historialPrecio).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
                }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }

    // Listar los historiales de precios
    fun listarHistorialPrecios(callback: (List<HistorialPrecio>?) -> Unit) {
        api.listarHistorialPrecios().enqueue(object : Callback<List<HistorialPrecio>> {
            override fun onResponse(call: Call<List<HistorialPrecio>>, response: Response<List<HistorialPrecio>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<HistorialPrecio>>, t: Throwable) {
                callback(null)
            }
        })
    }

    // Actualizar un historial de precio por ID
    fun actualizarHistorialPrecio(id: String, precio: Double, fechaInicio: Date, fechaFin: Date, estadoPrecio: Boolean, callback: (Boolean) -> Unit) {
        val historialPrecio = HistorialPrecio(
            precio = precio,
            fechaInicio = fechaInicio,
            fechaFin = fechaFin,
            estadoPrecio = estadoPrecio,
        )

        api.actualizarHistorialPrecio(id, historialPrecio).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }

    // Eliminar un historial de precio
    fun eliminarHistorialPrecio(id: String, callback: (Boolean) -> Unit) {
        api.eliminarHistorialPrecio(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }
}
