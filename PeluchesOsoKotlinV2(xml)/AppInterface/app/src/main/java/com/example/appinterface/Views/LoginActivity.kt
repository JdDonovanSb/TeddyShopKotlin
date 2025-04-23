package com.example.appinterface.Views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.R
import com.example.appinterface.Api.RetrofitClient
import com.example.appinterface.MainActivity
import com.example.appinterface.Models.Login.LoginRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.auth0.android.jwt.JWT

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Ingresa tus credenciales", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.authService.login(
                    LoginRequest(
                        email = email,
                        contraseña = password
                    )
                )

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { loginResponse ->
                            // Guardar el token y navegar a la siguiente pantalla
                            saveTokenAndNavigate(loginResponse.token)
                        } ?: run {
                            showError("Respuesta vacía del servidor")
                        }
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                        showError("Error: ${response.code()} - $errorMessage")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError("Error de conexión: ${e.message}")
                }
            }
        }
    }

    private fun saveTokenAndNavigate(token: String) {
        try {
            val jwt = JWT(token)

            // Obtiene el arreglo de roles y extrae el primero
            val roleList = jwt.getClaim("roles").asList(String::class.java)
            val role = roleList?.firstOrNull()

            if (role != null) {
                Toast.makeText(this, "Login exitoso. Rol: $role", Toast.LENGTH_SHORT).show()

                val intent = when (role.lowercase()) {
                    "administrador" -> Intent(this, AdministradorActivity::class.java)
                    "empleado" -> Intent(this, EmpleadoActivity::class.java)
                    else -> {
                        Toast.makeText(this, "Rol desconocido: $role", Toast.LENGTH_SHORT).show()
                        return
                    }
                }

                intent.putExtra("AUTH_TOKEN", token)
                startActivity(intent)
                finish()

            } else {
                showError("No se encontró el rol en el token")
            }

        } catch (e: Exception) {
            showError("Error al decodificar el token: ${e.message}")
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}