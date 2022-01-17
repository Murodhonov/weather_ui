package uz.umarxon.obhavo.models

data class Sys(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
) {
    override fun toString(): String {
        return "Sys(\ncountry='\n$country'\n,\n id=$id\n,\n sunrise=$sunrise\n,\n sunset=$sunset\n,\n type=$type\n)"
    }
}