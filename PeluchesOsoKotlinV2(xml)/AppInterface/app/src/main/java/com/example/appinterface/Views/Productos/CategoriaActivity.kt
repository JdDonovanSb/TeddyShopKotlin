package com.example.appinterface.Views.Productos

import Controller.Productos.CategoriaController
import Models.Productos.Categoria
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.R

class CategoriaActivity : AppCompatActivity() {
    private val categoriaController = CategoriaController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categoria)
    }

    fun crearCategoria(v: View) {
        val nombre = findViewById<EditText>(R.id.nombreCategoria).text.toString().trim()
        val descripcion = findViewById<EditText>(R.id.descripcionCategoria).text.toString().trim()

        if (nombre.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show()
            return
        }

        categoriaController.crearCategoria(nombre, descripcion, imagen = null, productos = null) { success ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Categoría creada con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al crear categoría", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun listarCategorias(v: View) {
        categoriaController.listarCategorias { categorias ->
            runOnUiThread {
                val textView = findViewById<TextView>(R.id.textViewListado)
                if (categorias != null && categorias.isNotEmpty()) {
                    textView.text = categorias.joinToString("\n") {
                        "ID: ${it.id ?: "Sin ID"}\nNombre: ${it.nombre}\nDescripción: ${it.descripcion}\n"
                    }
                } else {
                    textView.text = "No hay categorías disponibles"
                }
            }
        }
    }

}
