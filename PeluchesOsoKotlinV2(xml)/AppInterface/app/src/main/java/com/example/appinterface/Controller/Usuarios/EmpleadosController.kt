package Controller.Usuarios

import Models.Usuarios.Empleados

import com.example.appinterface.Api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class EmpleadosController {
    private val api = RetrofitClient.empleadoService

    // Crear un empleado
    fun crearEmpleado(dniEmpleado: Long, telefonoEmpleado: Long, nombreEmpleado: String, callback: (Boolean) -> Unit) {
        val empleado = Empleados(
            dniEmpleado = dniEmpleado,
            telefonoEmpleado = telefonoEmpleado,
            nombreEmpleado = nombreEmpleado)

        api.crearEmpleado(empleado).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun listarEmpleados(callback: (List<Empleados>?) -> Unit) {
        api.listarEmpleados().enqueue(object : Callback<List<Empleados>> {
            override fun onResponse(call: Call<List<Empleados>>, response: Response<List<Empleados>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    Log.e("LISTAR_EMPLEADOS", "Error en respuesta: ${response.code()} - ${response.message()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Empleados>>, t: Throwable) {
                Log.e("LISTAR_EMPLEADOS", "Error de red: ${t.message}")
                callback(null)
            }
        })
    }


    // Buscar empleado por ID
    fun obtenerEmpleadoPorId(id: String, callback: (Empleados?) -> Unit) {
        api.obtenerEmpleadoPorId(id).enqueue(object : Callback<Empleados> {
            override fun onResponse(call: Call<Empleados>, response: Response<Empleados>) {
                callback(response.body())
            }

            override fun onFailure(call: Call<Empleados>, t: Throwable) {
                callback(null)
            }
        })
    }

    // Actualizar empleado
    fun actualizarEmpleado(id: String, dniEmpleado: Long, telefonoEmpleado: Long, nombreEmpleado: String, callback: (Boolean) -> Unit) {
        val empleado = Empleados(
            dniEmpleado = dniEmpleado,
            telefonoEmpleado = telefonoEmpleado,
            nombreEmpleado = nombreEmpleado)

        api.actualizarEmpleado(id, empleado).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }

    // Eliminar empleado
    fun eliminarEmpleado(id: String, callback: (Boolean) -> Unit) {
        api.eliminarEmpleado(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }
}
