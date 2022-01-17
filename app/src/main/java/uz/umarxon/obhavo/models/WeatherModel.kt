package uz.umarxon.obhavo.models

import androidx.room.Entity

@Entity
data class WeatherModel(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
) {
    override fun toString(): String {
        return "WeatherModel(\nbase='$base'\n,\n clouds=$clouds\n,\n cod=$cod\n,\n coord=$coord\n,\n dt=$dt\n,\n id=$id\n,\n main=$main\n,\n name='$name'\n,\n sys=$sys\n,\n timezone=$timezone\n,\n visibility=$visibility\n,\n weather=$weather\n,\n wind=$wind)"
    }
}