package uz.umarxon.obhavo.models

data class Coord(
    val lat: Double,
    val lon: Double
) {
    override fun toString(): String {
        return "Coord(\nlat=$lat\n,\n lon=$lon\n)"
    }
}