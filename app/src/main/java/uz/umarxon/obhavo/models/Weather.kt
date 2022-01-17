package uz.umarxon.obhavo.models

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
) {
    override fun toString(): String {
        return "Weather(\ndescription='$description'\n,\n icon='$icon'\n,\n id=$id, main='$main'\n)"
    }
}