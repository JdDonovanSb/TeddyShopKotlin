package Controller.Pedidos

import Models.Pedidos.Factura
import android.annotation.SuppressLint

import android.util.Log
import com.example.appinterface.Api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FacturaController {
    private val api = RetrofitClient.facturaService

    fun crearFactura(fecha: String, hora: String, callback: (Boolean) -> Unit) {
        val factura = Factura(
            fechaCreacionFactura = fecha,
            horaCreacionFactura = hora
        )

        api.crearFactura(factura).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("FACTURA_CREAR_ERROR", "Error al crear factura: ${t.message}")
                callback(false)
            }
        })
    }

    fun actualizarFactura(id: String, fecha: String, hora: String, callback: (Boolean) -> Unit) {
        val factura = Factura(
            fechaCreacionFactura = fecha,
            horaCreacionFactura = hora
        )

        api.actualizarFactura(id, factura).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (!response.isSuccessful) {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ACTUALIZAR_ERROR", "Código: ${response.code()} - Error: $errorBody")
                }
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("FACTURA_ACTUALIZAR_FAIL", "Error de red: ${t.message}")
                callback(false)
            }
        })
    }

    fun listarFacturas(callback: (List<Factura>?) -> Unit) {
        api.listarFacturas().enqueue(object : Callback<List<Factura>> {
            override fun onResponse(call: Call<List<Factura>>, response: Response<List<Factura>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Factura>>, t: Throwable) {
                Log.e("FACTURA_LISTAR_ERROR", "Error de red: ${t.message}")
                callback(null)
            }
        })
    }

    fun eliminarFactura(id: String, callback: (Boolean) -> Unit) {
        api.eliminarFactura(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("FACTURA_ELIMINAR_FAIL", "Error de red: ${t.message}")
                callback(false)
            }
        })
    }
}
