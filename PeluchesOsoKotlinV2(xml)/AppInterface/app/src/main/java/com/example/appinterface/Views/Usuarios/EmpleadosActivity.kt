package com.example.appinterface.Views.Usuarios

import Controller.Usuarios.EmpleadosController
import Models.Usuarios.Empleados
import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.appinterface.R

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
        val dni = findViewById<EditText>(R.id.DniEmpleado).text.toString().toLongOrNull()
        val telefono = findViewById<EditText>(R.id.TelefonoEmpleado).text.toString().toLongOrNull()
        val nombre = findViewById<EditText>(R.id.NombreEmpleado).text.toString().trim()

        if (dni == null || telefono == null) {
            mostrarToast("Complete todos los campos")
            return
        }

        empleadosController.crearEmpleado(dni, telefono, nombre) { success ->
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
                    mostrarToast("Error al cargar empleados")
                    return@runOnUiThread
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

            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_edit_24)
                setOnClickListener { mostrarFormularioEdicion(empleado) }
                background = null
                addView(this)
            }

            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_delete_24)
                setOnClickListener { confirmarEliminacion(empleado) }
                background = null
                addView(this)
            }

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
        findViewById<LinearLayout>(R.id.layoutActualizarEmpleado).visibility = View.VISIBLE

        findViewById<EditText>(R.id.editTextNombreEmpleado).setText(empleado.nombreEmpleado)
        findViewById<EditText>(R.id.editTextDniEmpleado).setText(empleado.dniEmpleado.toString())
        findViewById<EditText>(R.id.editTextTelefonoEmpleado).setText(empleado.telefonoEmpleado.toString())
    }

    fun actualizarEmpleado(v: View) {
        val id = empleadoSeleccionadoId ?: return
        val nuevoNombre = findViewById<EditText>(R.id.editTextNombreEmpleado).text.toString().trim()
        val nuevoDniStr = findViewById<EditText>(R.id.editTextDniEmpleado).text.toString()
        val nuevoTelefonoStr = findViewById<EditText>(R.id.editTextTelefonoEmpleado).text.toString()

        val nuevoDni = nuevoDniStr.toLongOrNull()
        val nuevoTelefono = nuevoTelefonoStr.toLongOrNull()

        if (nuevoNombre.isEmpty() || nuevoDni == null || nuevoTelefono == null) {
            mostrarToast("Complete todos los campos de actualización")
            return
        }

        empleadosController.actualizarEmpleado(id, nuevoDni, nuevoTelefono, nuevoNombre) { success ->
            runOnUiThread {
                if (success) {
                    mostrarToast("Empleado actualizado correctamente")
                    findViewById<LinearLayout>(R.id.layoutActualizarEmpleado).visibility = View.GONE

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
