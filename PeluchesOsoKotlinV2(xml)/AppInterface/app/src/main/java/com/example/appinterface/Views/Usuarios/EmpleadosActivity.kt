package com.example.appinterface.Views.Usuarios

import Controller.Usuarios.EmpleadosController
import Models.Usuarios.Empleados
import Models.Usuarios.Usuario
import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.appinterface.R
import android.util.Log

class EmpleadosActivity : AppCompatActivity() {
    private val empleadosController = EmpleadosController()
    private var empleadoSeleccionadoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empleadocrud)
    }

    private fun createCell(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(16, 8, 16, 8)
            gravity = Gravity.CENTER
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        }
    }

    fun crearEmpleado(v: View) {
        val dni = findViewById<EditText>(R.id.DniEmpleado).text.toString().toIntOrNull()
        val telefono = findViewById<EditText>(R.id.TelefonoEmpleado).text.toString().toIntOrNull()
        val nombre = findViewById<EditText>(R.id.NombreEmpleado).text.toString().trim()

        if (dni == null || telefono == null || nombre.isEmpty()) {
            mostrarToast("Complete todos los campos")
            return
        }

        empleadosController.crearEmpleado(dni.toLong(), telefono.toLong(), nombre) { success ->
            runOnUiThread {
                if (success) {
                    mostrarToast("Empleado creado con éxito")
                    limpiarFormulario()
                    listarEmpleados(v)
                } else {
                    mostrarToast("Error al crear empleado")
                }
            }
        }
    }

    fun listarEmpleados(v: View) {
        empleadosController.listarEmpleados { empleados ->
            runOnUiThread {
                if (empleados == null) {
                    Log.e("LISTAR_EMPLEADOS", "La lista de empleados es null")
                    mostrarToast("Error al cargar empleados")
                    return@runOnUiThread
                }

                Log.d("LISTAR_EMPLEADOS", "Cantidad de empleados: ${empleados.size}")
                empleados.forEach { empleado ->
                    Log.d("LISTAR_EMPLEADOS", "Empleado: $empleado")
                }

                val table = findViewById<TableLayout>(R.id.tableEmpleados)
                table.removeViews(1, table.childCount - 1)

                empleados.forEach { empleado ->
                    val row = TableRow(this).apply {
                        addView(createCell(empleado.nombreEmpleado))
                        addView(createCell(empleado.dniEmpleado.toString()))
                        addView(createCell(empleado.telefonoEmpleado.toString()))
                        addView(crearBotonesAccion(empleado))
                    }
                    table.addView(row)
                }
            }
        }
    }

    private fun crearBotonesAccion(empleado: Empleados): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
            gravity = Gravity.CENTER

            // Botón Editar
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_edit_24)
                setOnClickListener { mostrarFormularioEdicion(empleado) }
                background = null
                addView(this)
            }

            // Botón Eliminar
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_delete_24)
                setOnClickListener { confirmarEliminacion(empleado) }
                background = null
                addView(this)
            }


            // Botón Detalles
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_info_24)
                setOnClickListener { verDetalles(empleado) }
                background = null
                addView(this)
            }
        }
    }

    private fun mostrarFormularioEdicion(empleado: Empleados) {
        empleadoSeleccionadoId = empleado.id
        findViewById<ConstraintLayout>(R.id.layoutActualizarEmpleado).visibility = View.VISIBLE

        findViewById<EditText>(R.id.editTextNombreEmpleado).setText(empleado.nombreEmpleado)
        findViewById<EditText>(R.id.editTextDniEmpleado).setText(empleado.dniEmpleado.toString())
        findViewById<EditText>(R.id.editTextTelefonoEmpleado).setText(empleado.telefonoEmpleado.toString())
    }

    fun actualizarEmpleado(v: View) {
        val id = empleadoSeleccionadoId ?: return
        val nuevoNombre = findViewById<EditText>(R.id.editTextNombreEmpleado).text.toString().trim()
        val nuevoDniStr = findViewById<EditText>(R.id.editTextDniEmpleado).text.toString()
        val nuevoTelefonoStr = findViewById<EditText>(R.id.editTextTelefonoEmpleado).text.toString()

        Log.d("ACTUALIZAR", "Nombre: $nuevoNombre, DNI: $nuevoDniStr, Teléfono: $nuevoTelefonoStr")

        val nuevoDni = nuevoDniStr.toIntOrNull()
        val nuevoTelefono = nuevoTelefonoStr.toIntOrNull()

        if (nuevoNombre.isEmpty() || nuevoDni == null || nuevoTelefono == null) {
            mostrarToast("Complete todos los campos de actualización")
            return
        }

        empleadosController.actualizarEmpleado(id, nuevoDni.toLong(), nuevoTelefono.toLong(), nuevoNombre) { success ->
            runOnUiThread {
                if (success) {
                    mostrarToast("Empleado actualizado correctamente")
                    findViewById<ConstraintLayout>(R.id.layoutActualizarEmpleado).visibility = View.GONE
                    listarEmpleados(v)
                } else {
                    mostrarToast("Error al actualizar empleado")
                }
            }
        }
    }

    private fun confirmarEliminacion(empleado: Empleados) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar empleado")
            .setMessage("¿Confirmar eliminación?")
            .setPositiveButton("Eliminar") { _, _ ->
                empleado.id?.let { id ->
                    empleadosController.eliminarEmpleado(id) { success ->
                        runOnUiThread {
                            if (success) {
                                mostrarToast("Empleado eliminado")
                                listarEmpleados(findViewById(R.id.listarEmpleados))
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

    private fun verDetalles(empleados: Empleados) {
        AlertDialog.Builder(this)
            .setTitle("Detalles del Empleado")
            .setMessage("""
                Dni: ${empleados.dniEmpleado}
                telefono: ${empleados.telefonoEmpleado}
                nombre empleado: ${empleados.nombreEmpleado}
            """.trimIndent())
            .setPositiveButton("Cerrar", null)
            .show()
    }


    private fun limpiarFormulario() {
        findViewById<EditText>(R.id.DniEmpleado).text.clear()
        findViewById<EditText>(R.id.TelefonoEmpleado).text.clear()
        findViewById<EditText>(R.id.NombreEmpleado).text.clear()
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}



