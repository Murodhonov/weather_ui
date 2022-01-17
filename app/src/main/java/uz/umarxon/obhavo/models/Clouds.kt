package uz.umarxon.obhavo.models

data class Clouds(
    val all: Int
) {
    override fun toString(): String {
        return "Clouds(\nall=$all\n)"
    }
}