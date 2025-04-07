package com.example.appinterface.Views.Pedidos;

import Controller.Pedidos.DetalleFacturaController;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appinterface.R;
import android.widget.Toast



class DetalleFacturaActivity : AppCompatActivity() {
    private val controller = DetalleFacturaController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detallefactura)
    }

    fun crearDetalleFactura(v: View) {
        val numDetalle = findViewById<EditText>(R.id.numDetalle).text.toString().toIntOrNull()
        val precio = findViewById<EditText>(R.id.precioDetalle).text.toString().toDoubleOrNull()
        val cantidad = findViewById<EditText>(R.id.cantidadDetalle).text.toString().toIntOrNull()

        if (numDetalle == null || precio == null || cantidad == null) {
            Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
            return
        }

        // Llamada al controlador sin numFactura
        controller.crearDetalleFactura(numDetalle, precio, cantidad) { success ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Detalle creado con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al crear el detalle", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun listarDetallesFactura(v: View) {
        controller.listarDetallesFactura { detalles ->
            runOnUiThread {
                val textView = findViewById<TextView>(R.id.textViewDetallesFactura)
                if (detalles != null && detalles.isNotEmpty()) {
                    textView.text = detalles.joinToString("\n") {
                        "Detalle: ${it.numDetalle}, Precio: ${it.precioDetalleFactura}, Cantidad: ${it.cantidadDetalleFactura}"
                    }
                } else {
                    textView.text = "No hay detalles disponibles"
                }
            }
        }
    }

    fun actualizarDetalleFactura(v: View) {
        val id = findViewById<EditText>(R.id.idDetalleFactura).text.toString().trim()
        val precio = findViewById<EditText>(R.id.precioDetalle).text.toString().toDoubleOrNull()
        val cantidad = findViewById<EditText>(R.id.cantidadDetalle).text.toString().toIntOrNull()

        if (id.isEmpty() || precio == null || cantidad == null) {
            Toast.makeText(this, "ID, precio y cantidad son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        controller.actualizarDetalleFactura(id, precio, cantidad) { success ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Detalle actualizado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun eliminarDetalleFactura(v: View) {
        val id = findViewById<EditText>(R.id.idDetalleFactura).text.toString().trim()

        if (id.isEmpty()) {
            Toast.makeText(this, "Ingresa un ID válido para eliminar", Toast.LENGTH_SHORT).show()
            return
        }

        controller.eliminarDetalleFactura(id) { success ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Detalle eliminado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}