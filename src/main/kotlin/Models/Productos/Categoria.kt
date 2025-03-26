package Models.Productos

class Categoria (private var IdCategoria: String, private var NombreCategoria: String, private var DescripcionCategoria: String){
    fun Categoria(idCategoria: String, nombreCategoria: String, descripcionCategoria: String){
        this.IdCategoria = idCategoria
        this.NombreCategoria = nombreCategoria
        this.DescripcionCategoria = descripcionCategoria
    }

    fun getNombreCategoria(): String {
        return NombreCategoria
    }

    fun getIdCategoria(): String{
        return IdCategoria
    }

    fun getDescripcionCategoria(): String {
        return DescripcionCategoria
    }

    fun setNombreCategoria(nombreCategoria: String) {
        this.NombreCategoria = nombreCategoria
    }

    fun setDescripcionCategoria(descripcionCategoria: String) {
        this.DescripcionCategoria = descripcionCategoria
    }
}