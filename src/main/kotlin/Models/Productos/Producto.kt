package Models.Productos

class Producto (private var IdProducto: String, private var EstiloProducto: String, private var MaterialProducto: String, private var TamañoProducto: String, private var DisponibilidadProducto: Boolean) {

    fun Producto(idProducto: String, estiloProducto: String, materialProducto: String, tamañoProducto: String, disponibildadProducto: Boolean){
        this.IdProducto = idProducto
        this.EstiloProducto = estiloProducto
        this.MaterialProducto = materialProducto
        this.TamañoProducto = tamañoProducto
        this.DisponibilidadProducto = disponibildadProducto
    }

    fun getIdProducto(): String {
        return IdProducto
    }

    fun getEstiloProducto(): String{
        return EstiloProducto
    }

    fun getMaterialProducto(): String{
        return MaterialProducto
    }

    fun getTamañoProducto(): String{
        return TamañoProducto
    }

    fun getDisponibilidadProducto(): Boolean{
        return DisponibilidadProducto
    }

    fun setIdProducto(idProducto: String){
        this.IdProducto = idProducto
    }

    fun setEstiloProducto(estiloProducto: String) {
        this.EstiloProducto = estiloProducto
    }

    fun setMaterialProducto(materialProducto: String) {
        this.MaterialProducto = materialProducto
    }

    fun setTamañoProducto(tamañoProducto: String) {
        this.TamañoProducto = tamañoProducto
    }

    fun setDisponibilidadProducto(disponibilidadProducto: Boolean) {
        this.DisponibilidadProducto = disponibilidadProducto
    }
}