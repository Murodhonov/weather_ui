package uz.umarxon.obhavo.Data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.umarxon.obhavo.models.WeatherModel

object MySharedPreference {
    private const val NAME = "forCache"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operations: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operations(editor)
        editor.apply()
    }

    var myList: ArrayList<WeatherModel>
        get() = Gson().fromJson(preferences.getString("weather", "[]")!!,
            object : TypeToken<ArrayList<WeatherModel>>() {}.type)
        set(value) = preferences.edit {
            it.putString("weather", Gson().toJson(value))
        }

}