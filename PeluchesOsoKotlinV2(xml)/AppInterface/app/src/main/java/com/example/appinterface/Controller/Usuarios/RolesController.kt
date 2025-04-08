package Controller.Usuarios

import Models.Productos.Producto
import Models.Usuarios.Roles
import android.util.Log
import com.example.appinterface.Api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RolesController {

    private val api = RetrofitClient.rolesService

    fun crearRol(estado: Boolean, nombre: String, usuarios: List<String>?, callback: (Boolean) -> Unit) {
        val roles = Roles(
            estado = estado,
            nombre = nombre,
            usuarios = usuarios ?: emptyList()
        )

        api.crearRoles(roles).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }


    fun listarRoles(callback: (List<Roles>?) -> Unit) {
        api.listarRoles().enqueue(object : Callback<List<Roles>> {
            override fun onResponse(call: Call<List<Roles>>, response: Response<List<Roles>>) {
                if(response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Roles>>, t: Throwable) {
                callback(null)
            }
        })
    }

    // Buscar un Rol por ID
    //fun buscarRolPorId(id: String): String {
    //    val rol = roles[id]
    //    return if (rol != null) {
    //        "Rol encontrado: Estado del rol: ${rol.getEstado()}, Nombre del rol: ${rol.getNombre()}"
    //    } else {
    //        "Rol no encontrado."
    //    }
    //}



    fun actualizarRol(id: String, estado: Boolean, nombre: String, callback: (Boolean) -> Unit) {
        val roles = Roles(
            estado = estado,
            nombre = nombre
        )

        api.actualizarRoles(id, roles).enqueue((object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(!response.isSuccessful) {
                    val errorBody = response.errorBody()?.toString()
                    Log.e("ACTUALIZAR_ERROR", "Código: ${response.code()} - Error: $errorBody")
                }
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ACTUALIZAR_FAILURE", "Error de red: ${t.message}")
                callback(false)
            }
        }))
    }

    fun eliminarRol(id: String, callback: (Boolean) -> Unit) {
        api.eliminarRoles(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }


}