package Controller.Pedidos

import Models.Pedidos.MetodoPago
import android.util.Log
import com.example.appinterface.Api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MetodoPagoController {

    private val api = RetrofitClient.metodoPagoService

    fun listarMetodosPago(callback: (List<MetodoPago>?) -> Unit) {
        api.listarMetodoPago().enqueue(object : Callback<List<MetodoPago>> {
            override fun onResponse(call: Call<List<MetodoPago>>, response: Response<List<MetodoPago>>) {
                if (response.isSuccessful) {
                    Log.d("MetodoPagoController", "Métodos de pago recibidos: ${response.body()}")
                    callback(response.body())
                } else {
                    Log.e("MetodoPagoController", "Respuesta fallida o vacía: ${response.errorBody()?.string()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<MetodoPago>>, t: Throwable) {
                Log.e("MetodoPagoController", "Error en listarMetodosPago", t)
                callback(null)
            }
        })
    }

    fun crearMetodoPago(metodoPago: MetodoPago, callback: (Boolean) -> Unit) {
        api.crearMetodoPago(metodoPago).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("MetodoPagoController", "Método de pago creado correctamente")
                } else {
                    Log.w("MetodoPagoController", "Error de respuesta: ${response.code()} - ${response.errorBody()?.string()}")
                }
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("MetodoPagoController", " Error al crear método de pago: ${t.localizedMessage}", t)
                callback(false)
            }
        })
    }

    fun actualizarMetodoPago(id: String, metodoPago: MetodoPago, callback: (Boolean) -> Unit) {
        api.actualizarMetodoPago(id, metodoPago).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (!response.isSuccessful) {
                    Log.w("MetodoPagoController", "Error al actualizar: ${response.code()} - ${response.errorBody()?.string()}")
                }
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("MetodoPagoController", "Error de red al actualizar", t)
                callback(false)
            }
        })
    }

    fun eliminarMetodoPago(id: String, callback: (Boolean) -> Unit) {
        api.eliminarMetodoPago(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("MetodoPagoController", "Método de pago eliminado - éxito: ${response.isSuccessful}")
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("MetodoPagoController", "Error al eliminar método de pago", t)
                callback(false)
            }
        })
    }
}
