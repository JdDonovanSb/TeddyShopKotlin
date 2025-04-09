package com.example.appinterface.Views.Usuarios
import Controller.Usuarios.RolesController
import Models.Pedidos.Devoluciones
import Models.Usuarios.Roles
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.appinterface.R

class RolesActivity: AppCompatActivity() {
    private val rolesController = RolesController()
    private var rolSeleccionado: Roles? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rol)
    }


    private fun createCell(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(16, 8, 16, 8)
            gravity = Gravity.CENTER
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        }
    }


    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }


    fun crearRol(v: View){
        val nombreRol = findViewById<EditText>(R.id.nombreRol).text.toString()
        val estadoRol = findViewById<Switch>(R.id.estadoRol).isChecked

        if(nombreRol.isEmpty()){
            mostrarToast("Complete todos los campos")
            return
        }

        rolesController.crearRol(estadoRol, nombreRol, usuarios = null) { success ->
            runOnUiThread {
                if(success) {
                    Toast.makeText(this, "Rol creado con exito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al crear el rol", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun listarRoles(v: View) {
        rolesController.listarRoles { roles ->
            runOnUiThread {
                val table = findViewById<TableLayout>(R.id.tableRoles)
                table.removeViews(1, table.childCount - 1)
                roles?.forEach { roles ->
                    val row = TableRow(this).apply {
                        addView(createCell(roles.nombre))
                        addView(createCell(if (roles.estado) "Activo" else "Inactivo"))
                        addView(crearBotonesAccion(roles))
                    }
                    table.addView(row)
                } ?: mostrarToast("Error al cargar los roles")
            }
        }
    }


    fun actualizarRol(v: View) {
        rolSeleccionado?.let {roles ->
            val nuevoNombre = findViewById<EditText>(R.id.editTextNombreRol).text.toString()
            val nuevoEstado = findViewById<Switch>(R.id.switchEstadoRol).isChecked


            rolesController.actualizarRol(
                roles.id ?: return,
                nuevoEstado,
                nuevoNombre
            ) { success -> runOnUiThread {
                if(success) {
                    mostrarToast("Actualizado correctamente")
                    findViewById<ConstraintLayout>(R.id.layoutActualizarRol).visibility = View.GONE
                    listarRoles(findViewById(R.id.listarRoles))
                } else {
                    mostrarToast("Error al actualizar")
                }
            }}
        }
    }


    fun confirmarEliminacion(roles: Roles) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Rol")
            .setMessage("¿Confirmar eliminación?")
            .setPositiveButton("Eliminar") { _, _ -> roles.id?.let {id ->
                rolesController.eliminarRol(id) { success -> runOnUiThread {
                    if(success) {
                        mostrarToast("Eliminado")
                        listarRoles(findViewById(R.id.listarRoles))
                    } else {
                        mostrarToast("Error al eliminar")
                    }
                }}
            }}
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun crearBotonesAccion(roles: Roles): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
            gravity = Gravity.CENTER

            // Botón Editar
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_edit_24)
                setOnClickListener { mostrarFormularioEdicion(roles)}
                background = null
                addView(this)
            }

            // Botón Eliminar
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_delete_24)
                setOnClickListener { confirmarEliminacion(roles)}
                background = null
                addView(this)
            }

            // Botón Detalles
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_info_24)
                setOnClickListener { verDetalles(roles)}
                background = null
                addView(this)
            }
        }
    }


    private fun mostrarFormularioEdicion(roles: Roles) {
        rolSeleccionado = roles
        findViewById<ConstraintLayout>(R.id.layoutActualizarRol).visibility = View.VISIBLE
        findViewById<EditText>(R.id.editTextNombreRol).setText(roles.nombre)
        findViewById<Switch>(R.id.estadoRol).isChecked = roles.estado
    }

    private fun verDetalles(roles: Roles) {
        android.app.AlertDialog.Builder(this)
            .setTitle("Detalles del Rol")
            .setMessage("""
             Nombre: ${roles.nombre}
             Estado: ${roles.estado}
         """.trimIndent())
            .setPositiveButton("Cerrar", null)
            .show()
    }
}