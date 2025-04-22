package com.example.appinterface.Views.Pedidos

import Controller.Pedidos.DevolucionesController
import Models.Pedidos.Devoluciones
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.appinterface.R

class DevolucionesActivity : AppCompatActivity() {
    private val devolucionesController = DevolucionesController()
    private var devolucionSeleccionada: Devoluciones? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devoluciones)
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

    fun crearDevolucion(v: View) {
        val detalleDevolucion = findViewById<EditText>(R.id.detalleDevolucion).text.toString()

        if (detalleDevolucion.isEmpty()) {
            mostrarToast("Complete todos los campos")
            return
        }
        devolucionesController.crearDevolucion(detalleDevolucion) { success ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Devolucion creada con exito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al crear la devolucion", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun listarDevoluciones(v: View) {
        devolucionesController.listarDevoluciones { devoluciones ->
            runOnUiThread {
                val table = findViewById<TableLayout>(R.id.tableDevoluciones)
                table.removeViews(1, table.childCount - 1)
                devoluciones?.forEach { devolucion ->
                    val row = TableRow(this).apply {
                        addView(createCell(devolucion.detalleDevolucion))
                        addView(crearBotonesAccion(devolucion))
                    }
                    table.addView(row)
                } ?: mostrarToast("Error al cargar devoluciones")
            }
        }
    }

    fun actualizarDevolucion(v: View) {
        devolucionSeleccionada?.let { devolucion ->
            val nuevoDetalleDevolucion = findViewById<EditText>(R.id.editTextDetalle).text.toString()

            devolucionesController.actualizarDevolucion(
                devolucion.id ?: return,
                nuevoDetalleDevolucion
            ) { success -> runOnUiThread {
                if(success) {
                    mostrarToast("Actualizado correctamente")
                    findViewById<LinearLayout>(R.id.layoutActualizarDevolucion).visibility = View.GONE
                    listarDevoluciones(findViewById(R.id.listarDevoluciones))
                } else {
                    mostrarToast("Error al actualizar")
                }
            }}
        }
    }

    fun confirmarEliminacion(devolucion: Devoluciones) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar devolución")
            .setMessage("¿Confirmar eliminación?")
            .setPositiveButton("Eliminar") { _, _ ->
                devolucion.id?.let { id ->
                    devolucionesController.eliminarDevolucion(id) { success ->
                        runOnUiThread {
                            if(success) {
                                mostrarToast("Eliminado")
                                listarDevoluciones(findViewById(R.id.listarDevoluciones))
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

    private fun crearBotonesAccion(devolucion: Devoluciones): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
            gravity = Gravity.CENTER

            // Botón Editar
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_edit_24)
                setOnClickListener { mostrarFormularioEdicion(devolucion) }
                background = null
                addView(this)
            }

            // Botón Eliminar
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_delete_24)
                setOnClickListener { confirmarEliminacion(devolucion) }
                background = null
                addView(this)
            }

            // Botón Detalles
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_info_24)
                setOnClickListener { verDetalles(devolucion) }
                background = null
                addView(this)
            }
        }
    }

    private fun mostrarFormularioEdicion(devolucion: Devoluciones) {
        devolucionSeleccionada = devolucion
        findViewById<LinearLayout>(R.id.layoutActualizarDevolucion).visibility = View.VISIBLE
        findViewById<EditText>(R.id.editTextDetalle).setText(devolucion.detalleDevolucion)
    }

    private fun verDetalles(devolucion: Devoluciones) {
        android.app.AlertDialog.Builder(this)
            .setTitle("Detalles de la Devolución")
            .setMessage("""
             Detalle: ${devolucion.detalleDevolucion}
         """.trimIndent())
            .setPositiveButton("Cerrar", null)
            .show()
    }
}
