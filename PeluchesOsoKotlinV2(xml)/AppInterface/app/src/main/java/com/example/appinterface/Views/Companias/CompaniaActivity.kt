package com.example.appinterface.Views.Companias

import Controller.CompaniaController
import Models.Compania
import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.R

class CompaniaActivity : AppCompatActivity() {
    private val companiaController = CompaniaController()
    private var companiaSeleccionada: Compania? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compania)
    }

    private fun createCell(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(16, 8, 16, 8)
            gravity = Gravity.CENTER
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        }
    }

    fun crearCompania(v: View) {
        val NIT = findViewById<EditText>(R.id.NITCompania).text.toString().toIntOrNull()
        val telefono = findViewById<EditText>(R.id.telefonoCompania).text.toString().trim()
        val nombre = findViewById<EditText>(R.id.nombreCompania).text.toString().trim()
        val direccion = findViewById<EditText>(R.id.direccionCompania).text.toString().trim()

        if (NIT == null || telefono.isEmpty() || nombre.isEmpty() || direccion.isEmpty()) {
            mostrarToast("Complete todos los campos")
            return
        }

        companiaController.crearCompania(NIT, telefono, nombre, direccion, catalogos = null, empleados = null) { success ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Compañía creada con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al crear la compañía", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun listarCompanias(v: View) {
        companiaController.listarCompañias { companias ->
            runOnUiThread {
                val table = findViewById<TableLayout>(R.id.tableCompanias)
                table.removeViews(1, table.childCount - 1)

                companias?.forEach { compania ->
                    val row = TableRow(this).apply {
                        addView(createCell(compania.nombreEmpresa))
                        addView(createCell(compania.telefonoEmpresa))
                        addView(crearBotonesAccion(compania))
                    }
                    table.addView(row)
                } ?: mostrarToast("Error al cargar compañías")
            }
        }
    }

    private fun crearBotonesAccion(compania: Compania): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
            gravity = Gravity.CENTER

            // Botón Editar
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_edit_24)
                setOnClickListener { mostrarFormularioEdicion(compania) }
                background = null
                addView(this)
            }

            // Botón Eliminar
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_delete_24)
                setOnClickListener { confirmarEliminacion(compania) }
                background = null
                addView(this)
            }

            // Botón Detalles
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_info_24)
                setOnClickListener { verDetalles(compania) }
                background = null
                addView(this)
            }
        }
    }

    private fun mostrarFormularioEdicion(compania: Compania) {
        companiaSeleccionada = compania
        findViewById<LinearLayout>(R.id.layoutActualizarCompania).visibility = View.VISIBLE

        findViewById<EditText>(R.id.editTextNombre).setText(compania.nombreEmpresa)
        findViewById<EditText>(R.id.editTextTelefono).setText(compania.telefonoEmpresa)
        findViewById<EditText>(R.id.editTextDireccion).setText(compania.direccionEmpresa)
    }

    fun actualizarCompania(v: View) {
        companiaSeleccionada?.let { compania ->
            val nuevoNombre = findViewById<EditText>(R.id.editTextNombre).text.toString()
            val nuevoTelefono = findViewById<EditText>(R.id.editTextTelefono).text.toString()
            val nuevaDireccion = findViewById<EditText>(R.id.editTextDireccion).text.toString()

            companiaController.actualizarCompania(
                compania.id ?: return,
                compania.NIT,
                nuevoTelefono,
                nuevoNombre,
                nuevaDireccion
            ) { success ->
                runOnUiThread {
                    if (success) {
                        mostrarToast("Actualizado correctamente")
                        findViewById<LinearLayout>(R.id.layoutActualizarCompania).visibility = View.GONE
                        listarCompanias(findViewById(R.id.listarCompanias))
                    } else {
                        mostrarToast("Error al actualizar")
                    }
                }
            }
        }
    }

    private fun confirmarEliminacion(compania: Compania) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar compañía")
            .setMessage("¿Confirmar eliminación?")
            .setPositiveButton("Eliminar") { _, _ ->
                compania.id?.let { id ->
                    companiaController.eliminarCompania(id) { success ->
                        runOnUiThread {
                            if (success) {
                                mostrarToast("Eliminada")
                                listarCompanias(findViewById(R.id.listarCompanias))
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

    private fun verDetalles(compania: Compania) {
        AlertDialog.Builder(this)
            .setTitle("Detalles de la Compañía")
            .setMessage("""
                Nombre: ${compania.nombreEmpresa}
                Teléfono: ${compania.telefonoEmpresa}
                Dirección: ${compania.direccionEmpresa}
            """.trimIndent())
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun limpiarFormulario() {
        findViewById<EditText>(R.id.NITCompania).text.clear()
        findViewById<EditText>(R.id.telefonoCompania).text.clear()
        findViewById<EditText>(R.id.nombreCompania).text.clear()
        findViewById<EditText>(R.id.direccionCompania).text.clear()
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}