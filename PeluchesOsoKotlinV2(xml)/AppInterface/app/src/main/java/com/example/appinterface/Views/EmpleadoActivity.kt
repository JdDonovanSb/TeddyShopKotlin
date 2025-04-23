package com.example.appinterface.Views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.R
import com.example.appinterface.Views.Pedidos.MainPedidoActivity
import com.example.appinterface.Views.Productos.MainProductosActivity

class EmpleadoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empleado)
    }

    fun mainProductoEmp(view: View) {
        val intent = Intent(this, MainProductosActivity::class.java)
        startActivity(intent)
    }

    fun mainPedidoEmp(view: View) {
        val intent = Intent(this, MainPedidoActivity::class.java)
        startActivity(intent)
    }
}
