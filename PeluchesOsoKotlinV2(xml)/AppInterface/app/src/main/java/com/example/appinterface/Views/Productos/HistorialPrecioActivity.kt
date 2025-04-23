package com.example.appinterface.Views.Productos

import Controller.Productos.HistorialPrecioController
import Models.Productos.HistorialPrecio
import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.appinterface.R
import java.text.SimpleDateFormat
import java.util.*

class HistorialPrecioActivity : AppCompatActivity() {

    private val historialController = HistorialPrecioController()
    private var historialSeleccionado: HistorialPrecio? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historialprecio)
    }

    private fun createCell(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(16, 8, 16, 8)
            gravity = Gravity.CENTER
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        }
    }

    fun crearHistorial(v: View) {
        val precioString = findViewById<EditText>(R.id.precio).text.toString().trim()
        val fechaInicioString = findViewById<EditText>(R.id.fechaInicio).text.toString().trim()
        val fechaFinString = findViewById<EditText>(R.id.fechaFin).text.toString().trim()
        val estado = findViewById<Switch>(R.id.estadoPrecio).isChecked

        if (precioString.isEmpty() || fechaInicioString.isEmpty() || fechaFinString.isEmpty()) {
            mostrarToast("Complete todos los campos")
            return
        }

        // Convertir precio a Double
        val precio = precioString.toDoubleOrNull()
        if (precio == null) {
            mostrarToast("El precio debe ser un número válido")
            return
        }

        // Convertir las fechas de String a Date
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaInicio = try {
            format.parse(fechaInicioString)
        } catch (e: Exception) {
            mostrarToast("Formato de fecha de inicio incorrecto")
            return
        }

        val fechaFin = try {
            format.parse(fechaFinString)
        } catch (e: Exception) {
            mostrarToast("Formato de fecha de fin incorrecto")
            return
        }

        historialController.crearHistorialPrecio(precio, fechaInicio, fechaFin, estado, null) { success ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Historial de precio creado con éxito", Toast.LENGTH_SHORT).show()
                    limpiarFormulario()
                } else {
                    Toast.makeText(this, "Error al crear el Historial de precio", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun listarHistorial(v: View) {
        historialController.listarHistorialPrecios { lista ->
            runOnUiThread {
                val tabla = findViewById<TableLayout>(R.id.tableHistorial)
                tabla.removeViews(1, tabla.childCount - 1)

                lista?.forEach { historial ->
                    val row = TableRow(this).apply {
                        addView(createCell(historial.precio.toString())) // Convertir precio a String
                        addView(createCell(historial.fechaInicio.toString())) // Convertir fecha a String
                        addView(createCell(if (historial.estadoPrecio) "Activo" else "Inactivo"))
                        addView(crearBotonesAccion(historial))
                    }
                    tabla.addView(row)
                } ?: mostrarToast("Error al cargar historial")
            }
        }
    }

    private fun crearBotonesAccion(historial: HistorialPrecio): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
            gravity = Gravity.CENTER

            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_edit_24)
                setOnClickListener { mostrarFormularioEdicion(historial) }
                background = null
                addView(this)
            }

            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_delete_24)
                setOnClickListener { confirmarEliminacion(historial) }
                background = null
                addView(this)
            }

            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_info_24)
                setOnClickListener { verDetalles(historial) }
                background = null
                addView(this)
            }
        }
    }

    private fun mostrarFormularioEdicion(historial: HistorialPrecio) {
        historialSeleccionado = historial
        findViewById<LinearLayout>(R.id.layoutActualizarHistorial).visibility = View.VISIBLE

        findViewById<EditText>(R.id.editTextPrecio).setText(historial.precio.toString())
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        findViewById<EditText>(R.id.editTextFechaInicio).setText(format.format(historial.fechaInicio))
        findViewById<EditText>(R.id.editTextFechaFin).setText(format.format(historial.fechaFin))
        findViewById<Switch>(R.id.switchEstadoPrecio).isChecked = historial.estadoPrecio
    }

    fun actualizarHistorial(v: View) {
        historialSeleccionado?.let { historial ->
            val nuevoPrecioString = findViewById<EditText>(R.id.editTextPrecio).text.toString()
            val nuevaFechaInicioString = findViewById<EditText>(R.id.editTextFechaInicio).text.toString()
            val nuevaFechaFinString = findViewById<EditText>(R.id.editTextFechaFin).text.toString()
            val nuevoEstado = findViewById<Switch>(R.id.switchEstadoPrecio).isChecked

            val nuevoPrecio = nuevoPrecioString.toDoubleOrNull()
            if (nuevoPrecio == null) {
                mostrarToast("El precio debe ser un número válido")
                return
            }

            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val nuevaFechaInicio = try {
                format.parse(nuevaFechaInicioString)
            } catch (e: Exception) {
                mostrarToast("Formato de fecha de inicio incorrecto")
                return
            }

            val nuevaFechaFin = try {
                format.parse(nuevaFechaFinString)
            } catch (e: Exception) {
                mostrarToast("Formato de fecha de fin incorrecto")
                return
            }

            historialController.actualizarHistorialPrecio(
                historial.id ?: return,
                nuevoPrecio,
                nuevaFechaInicio,
                nuevaFechaFin,
                nuevoEstado
            ) { success ->
                runOnUiThread {
                    if (success) {
                        mostrarToast("Actualizado correctamente")
                        findViewById<LinearLayout>(R.id.layoutActualizarHistorial).visibility = View.GONE
                        listarHistorial(findViewById(R.id.listarHistorial))
                    } else {
                        mostrarToast("Error al actualizar")
                    }
                }
            }
        }
    }

    private fun confirmarEliminacion(historial: HistorialPrecio) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar historial")
            .setMessage("¿Confirmar eliminación?")
            .setPositiveButton("Eliminar") { _, _ ->
                historial.id?.let { id ->
                    historialController.eliminarHistorialPrecio(id) { success ->
                        runOnUiThread {
                            if (success) {
                                mostrarToast("Historial eliminado")
                                listarHistorial(findViewById(R.id.listarHistorial))
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

    private fun verDetalles(historial: HistorialPrecio) {
        AlertDialog.Builder(this)
            .setTitle("Detalles del Historial")
            .setMessage("""
                Precio: ${historial.precio}
                Fecha inicio: ${historial.fechaInicio}
                Fecha fin: ${historial.fechaFin}
                Estado: ${if (historial.estadoPrecio) "Activo" else "Inactivo"}
            """.trimIndent())
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun limpiarFormulario() {
        findViewById<EditText>(R.id.precio).text.clear()
        findViewById<EditText>(R.id.fechaInicio).text.clear()
        findViewById<EditText>(R.id.fechaFin).text.clear()
        findViewById<Switch>(R.id.estadoPrecio).isChecked = false
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
