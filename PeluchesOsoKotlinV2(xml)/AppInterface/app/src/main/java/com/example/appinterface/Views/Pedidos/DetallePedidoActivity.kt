package com.example.appinterface.Views.Pedidos

import Controller.Pedidos.DetallePedidoController
import Models.Pedidos.DetallePedido
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.R
import android.util.Log

class DetallePedidoActivity : AppCompatActivity() {
    private val detallePedidoController = DetallePedidoController()
    private var detalleSeleccionado: DetallePedido? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Lifecycle", "DetallePedidoActivity onCreate")
        setContentView(R.layout.activity_detallepedido)
    }

    private fun createCell(text: String): TextView {
        Log.d("UI", "Creando celda con texto: $text")
        return TextView(this).apply {
            this.text = text
            setPadding(16, 8, 16, 8)
            gravity = Gravity.CENTER
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        }
    }

    private fun mostrarToast(mensaje: String) {
        Log.d("Toast", mensaje)
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    fun crearDetallePedido(v: View) {
        Log.d("Accion", "crearDetallePedido clicado")
        val numDetalle = findViewById<EditText>(R.id.numDetallePedido).text.toString().toIntOrNull()
        val precio = findViewById<EditText>(R.id.precioDetallePedido).text.toString().toDoubleOrNull()
        val cantidad = findViewById<EditText>(R.id.cantidadDetallePedido).text.toString().toIntOrNull()

        Log.d("CrearDetalle", "Datos capturados -> num=$numDetalle, precio=$precio, cantidad=$cantidad")

        if (numDetalle == null || precio == null || cantidad == null) {
            mostrarToast("Complete todos los campos correctamente")
            Log.e("CrearDetalle", "Campos inválidos o vacíos")
            return
        }

        detallePedidoController.crearDetallePedido(numDetalle, precio, cantidad) { success ->
            Log.d("Retrofit", "Callback crearDetallePedido ejecutado, success=$success")
            runOnUiThread {
                if (success) {
                    mostrarToast("Detalle creado con éxito")
                    listarDetallesPedido(findViewById(R.id.listarDetallesPedido))
                } else {
                    mostrarToast("Error al crear detalle")
                }
            }
        }
    }

    fun listarDetallesPedido(v: View) {
        Log.d("Accion", "listarDetallesPedido clicado")
        detallePedidoController.listarDetallesPedido { detalles ->
            Log.d("Retrofit", "Callback listarDetallesPedido ejecutado")
            runOnUiThread {
                val table = findViewById<TableLayout>(R.id.tableDetallesPedido)
                table.removeViews(1, table.childCount - 1)
                if (detalles == null) {
                    Log.e("ListarDetalles", "Detalles es null")
                    mostrarToast("Error al cargar detalles")
                    return@runOnUiThread
                }

                Log.d("ListarDetalles", "Detalles recibidos: ${detalles.size}")
                detalles.forEach { detalle ->
                    Log.d("ListarDetalles", "Detalle: $detalle")
                    val row = TableRow(this).apply {
                        addView(createCell(detalle.numDetallePedido.toString()))
                        addView(createCell(detalle.precioDetallePedido.toString()))
                        addView(createCell(detalle.cantidadDetallePedido.toString()))
                        addView(crearBotonesAccion(detalle))
                    }
                    table.addView(row)
                }
            }
        }
    }

    private fun crearBotonesAccion(detalle: DetallePedido): LinearLayout {
        Log.d("UI", "Creando botones para detalle ID=${detalle.id}")
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
            gravity = Gravity.CENTER

            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_edit_24)
                setOnClickListener {
                    Log.d("BotonEditar", "Clic en editar ID=${detalle.id}")
                    mostrarFormularioEdicion(detalle)
                }
                background = null
                addView(this)
            }

            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_delete_24)
                setOnClickListener {
                    Log.d("BotonEliminar", "Clic en eliminar ID=${detalle.id}")
                    confirmarEliminacion(detalle)
                }
                background = null
                addView(this)
            }

            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_info_24)
                setOnClickListener {
                    Log.d("BotonInfo", "Clic en ver detalles ID=${detalle.id}")
                    verDetalles(detalle)
                }
                background = null
                addView(this)
            }
        }
    }

    private fun mostrarFormularioEdicion(detalle: DetallePedido) {
        Log.d("UI", "Mostrando formulario de edición para ID=${detalle.id}")
        detalleSeleccionado = detalle
        findViewById<LinearLayout>(R.id.layoutActualizarDetallePedido).visibility = View.VISIBLE
        findViewById<EditText>(R.id.editTextActualizarDetalleNum).setText(detalle.numDetallePedido.toString())
        findViewById<EditText>(R.id.editTextActualizarPrecio).setText(detalle.precioDetallePedido.toString())
        findViewById<EditText>(R.id.editTextActualizarCantidad).setText(detalle.cantidadDetallePedido.toString())
    }

    fun actualizarDetallePedido(v: View) {
        Log.d("Accion", "actualizarDetallePedido clicado")
        detalleSeleccionado?.let { detalle ->
            val nuevoNumDetalle = findViewById<EditText>(R.id.editTextActualizarDetalleNum).text.toString().toIntOrNull()
            val nuevoPrecio = findViewById<EditText>(R.id.editTextActualizarPrecio).text.toString().toDoubleOrNull()
            val nuevaCantidad = findViewById<EditText>(R.id.editTextActualizarCantidad).text.toString().toIntOrNull()

            Log.d("ActualizarDetalle", "Datos actualizados: ID=${detalle.id}, nuevoNum=$nuevoNumDetalle, nuevoPrecio=$nuevoPrecio, nuevaCantidad=$nuevaCantidad")

            if (nuevoPrecio == null || nuevaCantidad == null) {
                mostrarToast("Complete todos los campos")
                Log.e("ActualizarDetalle", "Campos de edición inválidos")
                return
            }

            detallePedidoController.actualizarDetallePedido(detalle.id ?: return, nuevoNumDetalle, nuevoPrecio, nuevaCantidad) { success ->
                Log.d("Retrofit", "Callback actualizarDetallePedido ejecutado, success=$success")
                runOnUiThread {
                    if (success) {
                        mostrarToast("Actualizado correctamente")
                        findViewById<LinearLayout>(R.id.layoutActualizarDetallePedido).visibility = View.GONE
                        listarDetallesPedido(findViewById(R.id.listarDetallesPedido))
                    } else {
                        mostrarToast("Error al actualizar")
                    }
                }
            }
        } ?: Log.e("ActualizarDetalle", "detalleSeleccionado es null")
    }

    private fun confirmarEliminacion(detalle: DetallePedido) {
        Log.d("Eliminar", "Confirmar eliminación para ID=${detalle.id}")
        AlertDialog.Builder(this)
            .setTitle("Eliminar detalle de pedido")
            .setMessage("¿Confirmar eliminación?")
            .setPositiveButton("Eliminar") { _, _ ->
                detalle.id?.let { id ->
                    detallePedidoController.eliminarDetallePedido(id) { success ->
                        Log.d("Retrofit", "Callback eliminarDetallePedido ejecutado, success=$success")
                        runOnUiThread {
                            if (success) {
                                mostrarToast("Eliminado")
                                listarDetallesPedido(findViewById(R.id.listarDetallesPedido))
                            } else {
                                mostrarToast("Error al eliminar")
                            }
                        }
                    }
                } ?: Log.e("Eliminar", "ID del detalle es null")
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun verDetalles(detalle: DetallePedido) {
        Log.d("Detalles", "Visualizando detalle: $detalle")
        AlertDialog.Builder(this)
            .setTitle("Detalle de Pedido")
            .setMessage("""
                Número: ${detalle.numDetallePedido}
                Precio: ${detalle.precioDetallePedido}
                Cantidad: ${detalle.cantidadDetallePedido}
            """.trimIndent())
            .setPositiveButton("Cerrar", null)
            .show()
    }
}
