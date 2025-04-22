package Controller

import Models.Compania
import com.example.appinterface.Api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompaniaController {
    private val api = RetrofitClient.companiaService

    // Crear una nueva compañía
    fun crearCompania(NIT: Int, telefonoEmpresa: String, nombreEmpresa: String, direccionEmpresa: String,
                      catalogos: List<String>?, empleados: List<String>?, callback: (Boolean) -> Unit) {
        val compania = Compania(
            NIT = NIT,
            telefonoEmpresa = telefonoEmpresa,
            nombreEmpresa = nombreEmpresa,
            direccionEmpresa = direccionEmpresa,
            //catalogos = catalogos ?: emptyList(),
            //empleados = empleados ?: emptyList()
        )

        api.crearCompañia(compania).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    println("Error en crear Compañia: ${response.errorBody()?.string()}")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Error de red en crear Compañia: ${t.message}")
                callback(false)
            }
        })
    }

    // Listar todas las compañías
    fun listarCompañias(callback: (List<Compania>?) -> Unit) {
        api.listarCompañias().enqueue(object : Callback<List<Compania>> {
            override fun onResponse(call: Call<List<Compania>>, response: Response<List<Compania>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    println("Error al listar compañías: ${response.errorBody()?.string()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Compania>>, t: Throwable) {
                println("Error de red al listar compañías: ${t.message}")
                callback(null)
            }
        })
    }

    // Actualizar compañía
    fun actualizarCompania(id: String, NIT: Int?, telefonoEmpresa: String, nombreEmpresa: String, direccionEmpresa: String, callback: (Boolean) -> Unit) {
        val compania = Compania(
            NIT = NIT,
            telefonoEmpresa = telefonoEmpresa,
            nombreEmpresa = nombreEmpresa,
            direccionEmpresa = direccionEmpresa
        )

        api.actualizarCompañia(id, compania).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }

    // Eliminar compañía
    fun eliminarCompania(id: String, callback: (Boolean) -> Unit) {
        api.eliminarCompañia(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }
}
