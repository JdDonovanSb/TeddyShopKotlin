import java.sql.Connection
import java.sql.DriverManager

class ConexionBD {
    private val url = "jdbc:sqlserver://localhost:1433;databaseName=TeddyShop;encrypt=true;trustServerCertificate=true"
    private val usuario = "sa"
    private val contraseña = "P3luch35.0s0"

    init {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error al cargar el driver de la base de datos.")
        }
    }

    fun getConnection(): Connection? {
        return try {
            DriverManager.getConnection(url, usuario, contraseña)
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error al conectar a la base de datos.")
            null
        }
    }
}
