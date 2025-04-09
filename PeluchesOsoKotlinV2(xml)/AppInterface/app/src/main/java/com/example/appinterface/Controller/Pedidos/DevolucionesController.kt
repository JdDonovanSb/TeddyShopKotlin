package Controller.Pedidos
import Models.Pedidos.Devoluciones
import android.util.Log
import com.example.appinterface.Api.RetrofitClient
import retrofit2.*

class DevolucionesController {

    private val api = RetrofitClient.devolucionesService

    fun crearDevolucion( detalleDevolucion: String, callback: (Boolean) -> Unit) {
        val devolucion = Devoluciones(
            detalleDevolucion = detalleDevolucion,
            //inventarios = inventarios ?: emptyList()
        )

        api.crearDevoluciones(devolucion).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }


    fun listarDevoluciones(callback: (List<Devoluciones>?) -> Unit) {
        api.listartDevoluciones().enqueue(object : Callback<List<Devoluciones>> {
            override fun onResponse(call: Call<List<Devoluciones>>, response: Response<List<Devoluciones>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Devoluciones>>, t: Throwable) {
                callback(null)
            }
        })
    }


    //fun buscarDevolucionPorId(id: String): String {
    //    val devolucion = devoluciones[id]
    //    return if (devolucion != null) {
    //        "Devolución encontrada: Detalles de la Devolución: ${devolucion.getDetalleDevolucion()}"
    //    } else {
    //        "Devolución no encontrada."
    //    }
    //}


    fun actualizarDevolucion(id: String, detalleDevolucion: String, callback: (Boolean) -> Unit) {
        val devolucion = Devoluciones(
            detalleDevolucion = detalleDevolucion,
        )

        api.actualizarDevoluciones(id, devolucion).enqueue(object : Callback<Void> {
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

    fun eliminarDevolucion(id: String, callback: (Boolean) -> Unit) {
        api.eliminarDevoluciones(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }
}
