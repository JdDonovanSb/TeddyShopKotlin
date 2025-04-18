package com.example.appinterface.Views.Usuarios

import Controller.Usuarios.RolesController
import Models.Usuarios.Roles
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.R

class RolesActivity : AppCompatActivity() {
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

    fun crearRol(v: View) {
        val nombreRol = findViewById<EditText>(R.id.nombreRol).text.toString()
        val estadoRol = findViewById<Switch>(R.id.estadoRol).isChecked

        if (nombreRol.isEmpty()) {
            mostrarToast("Complete todos los campos")
            return
        }

        rolesController.crearRol(estadoRol, nombreRol, usuarios = null) { success ->
            runOnUiThread {
                if (success) {
                    mostrarToast("Rol creado con éxito")
                    listarRoles(findViewById(R.id.listarRoles))
                } else {
                    mostrarToast("Error al crear el rol")
                }
            }
        }
    }

    fun listarRoles(v: View) {
        rolesController.listarRoles { roles ->
            runOnUiThread {
                val table = findViewById<TableLayout>(R.id.tableRoles)
                table.removeViews(1, table.childCount - 1)
                roles?.forEach { rol ->
                    val row = TableRow(this).apply {
                        addView(createCell(rol.nombre))
                        addView(createCell(if (rol.estado) "Activo" else "Inactivo"))
                        addView(crearBotonesAccion(rol))
                    }
                    table.addView(row)
                } ?: mostrarToast("Error al cargar los roles")
            }
        }
    }

    fun actualizarRol(v: View) {
        rolSeleccionado?.let { rol ->
            val nuevoNombre = findViewById<EditText>(R.id.editTextNombreRol).text.toString()
            val nuevoEstado = findViewById<Switch>(R.id.estadoRol).isChecked

            rolesController.actualizarRol(
                rol.id ?: return,
                nuevoEstado,
                nuevoNombre
            ) { success ->
                runOnUiThread {
                    if (success) {
                        mostrarToast("Actualizado correctamente")
                        findViewById<LinearLayout>(R.id.layoutActualizarRol).visibility = View.GONE
                        listarRoles(findViewById(R.id.listarRoles))
                    } else {
                        mostrarToast("Error al actualizar")
                    }
                }
            }
        }
    }

    fun confirmarEliminacion(rol: Roles) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Rol")
            .setMessage("¿Confirmar eliminación?")
            .setPositiveButton("Eliminar") { _, _ ->
                rol.id?.let { id ->
                    rolesController.eliminarRol(id) { success ->
                        runOnUiThread {
                            if (success) {
                                mostrarToast("Eliminado")
                                listarRoles(findViewById(R.id.listarRoles))
                            } else {
                                mostrarToast("Error al eliminar")
                            }
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun crearBotonesAccion(rol: Roles): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
            gravity = Gravity.CENTER

            // Botón Editar
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_edit_24)
                setOnClickListener { mostrarFormularioEdicion(rol) }
                background = null
                addView(this)
            }

            // Botón Eliminar
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_delete_24)
                setOnClickListener { confirmarEliminacion(rol) }
                background = null
                addView(this)
            }

            // Botón Detalles
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_info_24)
                setOnClickListener { verDetalles(rol) }
                background = null
                addView(this)
            }
        }
    }

    private fun mostrarFormularioEdicion(rol: Roles) {
        rolSeleccionado = rol
        findViewById<LinearLayout>(R.id.layoutActualizarRol).visibility = View.VISIBLE
        findViewById<EditText>(R.id.editTextNombreRol).setText(rol.nombre)
        findViewById<Switch>(R.id.estadoRol).isChecked = rol.estado
    }

    private fun verDetalles(rol: Roles) {
        AlertDialog.Builder(this)
            .setTitle("Detalles del Rol")
            .setMessage(
                """
                Nombre: ${rol.nombre}
                Estado: ${if (rol.estado) "Activo" else "Inactivo"}
            """.trimIndent()
            )
            .setPositiveButton("Cerrar", null)
            .show()
    }
}
