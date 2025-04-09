package Controller.Productos

import Models.Productos.Movimiento
import android.util.Log
import com.example.appinterface.Api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovimientoController {

    private val api = RetrofitClient.movimientoService

    fun listarMovimientos(callback: (List<Movimiento>?) -> Unit) {
        api.listarMovimientos().enqueue(object : Callback<List<Movimiento>> {
            override fun onResponse(call: Call<List<Movimiento>>, response: Response<List<Movimiento>>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("MovimientoController", "Movimientos recibidos: ${response.body()}")
                    callback(response.body())
                } else {
                    Log.e("MovimientoController", "Respuesta fallida o vacía: ${response.errorBody()?.string()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Movimiento>>, t: Throwable) {
                Log.e("MovimientoController", "Error en listarMovimientos", t)
                callback(null)
            }
        })
    }
    fun crearMovimiento(movimiento: Movimiento, callback: (Boolean) -> Unit) {
        // Asegurarse de enviar inventario como null si es un string vacío o "1" placeholder
        val movimientoAdaptado = movimiento.copy(
            inventario = if (movimiento.inventario.isNullOrBlank() || movimiento.inventario == "1") null else movimiento.inventario
        )

        api.crearMovimiento(movimientoAdaptado).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("MovimientoController", "✅ Movimiento creado correctamente")
                } else {
                    Log.w("MovimientoController", "⚠️ Error de respuesta: ${response.code()} - ${response.errorBody()?.string()}")
                }
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("MovimientoController", "❌ Error al crear movimiento: ${t.localizedMessage}", t)
                callback(false)
            }
        })
    }


    fun actualizarMovimiento(id: String, movimiento: Movimiento, callback: (Boolean) -> Unit) {
        api.actualizarMovimiento(id, movimiento).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (!response.isSuccessful) {
                    Log.w("MovimientoController", "Error al actualizar: ${response.code()} - ${response.errorBody()?.string()}")
                }
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("MovimientoController", "Error de red al actualizar", t)
                callback(false)
            }
        })
    }

    fun eliminarMovimiento(id: String, callback: (Boolean) -> Unit) {
        api.eliminarMovimiento(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("MovimientoController", "Movimiento eliminado - success: ${response.isSuccessful}")
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("MovimientoController", "Error al eliminar movimiento", t)
                callback(false)
            }
        })
    }
}
