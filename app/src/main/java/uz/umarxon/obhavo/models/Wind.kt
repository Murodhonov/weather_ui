package uz.umarxon.obhavo.models

data class Wind(
    val deg: Int,
    val gust: Double,
    val speed: Double


) {
    override fun toString(): String {
        return "Wind(\ndeg=$deg\n,\n gust=$gust\n,\n speed=$speed\n)"
    }
}