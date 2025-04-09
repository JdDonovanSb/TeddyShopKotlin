package com.example.appinterface.Views.Productos

import Controller.Productos.MovimientoController
import Models.Productos.Movimiento
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.appinterface.R

class MovimientoActivity : AppCompatActivity() {

    private val controller = MovimientoController()
    private var movimientoSeleccionado: Movimiento? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movimiento)

        val tabla = findViewById<TableLayout>(R.id.tableMovimientos)
        Log.d("MovimientoActivity", "Tabla encontrada: ${tabla != null}")
    }

    private fun createCell(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(16, 8, 16, 8)
            gravity = Gravity.CENTER
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        }
    }

    fun crearMovimiento(v: View) {
        val fecha = findViewById<EditText>(R.id.fechaMovimiento).text.toString().trim()
        val ingreso = findViewById<EditText>(R.id.cantidadIngreso).text.toString().trim()
        val venta = findViewById<EditText>(R.id.cantidadVendida).text.toString().trim()

        if (fecha.isEmpty() || ingreso.isEmpty() || venta.isEmpty()) {
            mostrarToast("Complete todos los campos")
            return
        }

        val ingresoInt = ingreso.toIntOrNull()
        val ventaInt = venta.toIntOrNull()

        if (ingresoInt == null || ventaInt == null) {
            mostrarToast("Ingreso y venta deben ser números válidos")
            return
        }

        val nuevoMovimiento = Movimiento(
            id = null,
            fecha = fecha,
            cantidadIngreso = ingresoInt,
            cantidadVendida = ventaInt,
            inventario = null // ← mandamos null para que el backend lo acepte
        )

        controller.crearMovimiento(nuevoMovimiento) { success ->
            runOnUiThread {
                if (success) {
                    mostrarToast("Movimiento creado")
                    limpiarFormulario()
                    listarMovimientos()
                } else {
                    mostrarToast("Error al crear")
                }
            }
        }
    }


    fun listarMovimientos(v: View? = null) {
        controller.listarMovimientos { lista ->
            runOnUiThread {
                val table = findViewById<TableLayout>(R.id.tableMovimientos)

                if (table.childCount > 1) {
                    table.removeViews(1, table.childCount - 1)
                }

                Log.d("MovimientoActivity", "Lista recibida: $lista")

                lista?.forEach { movimiento ->
                    val row = TableRow(this).apply {
                        addView(createCell(movimiento.fecha))
                        addView(createCell(movimiento.cantidadIngreso.toString()))
                        addView(createCell(movimiento.cantidadVendida.toString()))
                        addView(crearBotonesAccion(movimiento))
                    }
                    table.addView(row)
                } ?: mostrarToast("No hay movimientos disponibles")
            }
        }
    }

    private fun crearBotonesAccion(mov: Movimiento): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
            gravity = Gravity.CENTER

            val btnEdit = ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_edit_24)
                setOnClickListener { mostrarFormularioEdicion(mov) }
                background = null
            }

            val btnDelete = ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_delete_24)
                setOnClickListener { confirmarEliminacion(mov) }
                background = null
            }

            val btnInfo = ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_info_24)
                setOnClickListener { verDetalles(mov) }
                background = null
            }

            addView(btnEdit)
            addView(btnDelete)
            addView(btnInfo)
        }
    }

    private fun mostrarFormularioEdicion(mov: Movimiento) {
        movimientoSeleccionado = mov
        findViewById<LinearLayout>(R.id.layoutActualizarMovimiento).visibility = View.VISIBLE

        findViewById<EditText>(R.id.editTextFecha).setText(mov.fecha)
        findViewById<EditText>(R.id.editTextIngreso).setText(mov.cantidadIngreso.toString())
        findViewById<EditText>(R.id.editTextVenta).setText(mov.cantidadVendida.toString())
    }


    fun actualizarMovimiento(v: View) {
        movimientoSeleccionado?.let { mov ->
            val nuevaFecha = findViewById<EditText>(R.id.editTextFecha).text.toString().trim()
            val nuevoIngreso = findViewById<EditText>(R.id.editTextIngreso).text.toString().trim()
            val nuevaVenta = findViewById<EditText>(R.id.editTextVenta).text.toString().trim()

            if (nuevaFecha.isEmpty() || nuevoIngreso.isEmpty() || nuevaVenta.isEmpty()) {
                mostrarToast("Complete los campos de actualización")
                return
            }

            val actualizado = Movimiento(
                id = null, // Excluir _id del cuerpo del JSON
                fecha = nuevaFecha,
                cantidadIngreso = nuevoIngreso.toInt(),
                cantidadVendida = nuevaVenta.toInt(),
                inventario = mov.inventario // mantener la relación
            )

            // Usar el ID en la URL
            controller.actualizarMovimiento(mov.id ?: return, actualizado) { success ->
                runOnUiThread {
                    if (success) {
                        mostrarToast("Movimiento actualizado correctamente")
                        findViewById<View>(R.id.layoutActualizarMovimiento).visibility = View.GONE
                        listarMovimientos()
                    } else {
                        mostrarToast("Error al actualizar el movimiento")
                    }
                }
            }
        }
    }





    private fun confirmarEliminacion(mov: Movimiento) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar movimiento")
            .setMessage("¿Desea eliminar este movimiento?")
            .setPositiveButton("Eliminar") { _, _ ->
                controller.eliminarMovimiento(mov.id ?: return@setPositiveButton) { success ->
                    runOnUiThread {
                        if (success) {
                            mostrarToast("Movimiento eliminado")
                            listarMovimientos()
                        } else {
                            mostrarToast("Error al eliminar")
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun verDetalles(mov: Movimiento) {
        AlertDialog.Builder(this)
            .setTitle("Detalles del Movimiento")
            .setMessage("""
                Fecha: ${mov.fecha}
                Ingreso: ${mov.cantidadIngreso}
                Venta: ${mov.cantidadVendida}
            """.trimIndent())
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun limpiarFormulario() {
        findViewById<EditText>(R.id.fechaMovimiento).text.clear()
        findViewById<EditText>(R.id.cantidadIngreso).text.clear()
        findViewById<EditText>(R.id.cantidadVendida).text.clear()
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
