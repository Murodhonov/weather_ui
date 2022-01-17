package uz.umarxon.obhavo.models

data class Main(
    val feels_like: Double,
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
) {
    override fun toString(): String {
        return "Main(\nfeels_like=$feels_like\n, grnd_level=$grnd_level,\n humidity=$humidity\n, pressure=$pressure\n, sea_level=$sea_level\n, temp=$temp\n, temp_max=$temp_max\n, temp_min=$temp_min)"
    }
}