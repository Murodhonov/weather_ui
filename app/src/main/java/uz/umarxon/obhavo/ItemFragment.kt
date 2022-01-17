package uz.umarxon.obhavo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.github.matteobattilana.weather.PrecipType
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import uz.umarxon.obhavo.Data.MySharedPreference
import uz.umarxon.obhavo.databinding.FragmentItemBinding
import uz.umarxon.obhavo.models.WeatherModel
import java.sql.Date
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

class ItemFragment(var lat:Double,var lon:Double) : Fragment() {

    lateinit var binding:FragmentItemBinding
    var MY_API_KEY = ""
    lateinit var requestQueue:RequestQueue

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentItemBinding.inflate(layoutInflater)

        requestQueue = Volley.newRequestQueue(binding.root.context)

        return binding.root
    }

    private fun loading(lat: Double, lon: Double, unit: String) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${MY_API_KEY}&units=${unit}",
            null,
            { response ->
                if (response != null) {
                    val model = Gson().fromJson(response.toString(), WeatherModel::class.java)

                    val updatedAt:Long = model.dt.toLong()

                    val sunrise:Long = model.sys.sunrise.toLong()
                    val sunset:Long = model.sys.sunset.toLong()

                    binding.updatedAt.text = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a").format(Date(updatedAt*1000))

                    binding.address.text = model.name
                    binding.status.text = model.weather[0].description

                    binding.temp.text = "${model.main.temp.roundToInt()}°C"
                    binding.tempMin.text  = "Min Temp: ${model.main.temp_min.roundToInt()}°C"
                    binding.tempMax.text  = "Max Temp: ${model.main.temp_max.roundToInt()}°C"


                    binding.pressure.text = model.main.pressure.toString()
                    binding.humidity.text = model.main.humidity.toString()+"%"
                    binding.wind.text = model.wind.speed.toString()+"k/h"

                    binding.sunrise.text = SimpleDateFormat("hh:mm a").format(Date(sunrise*1000))
                    binding.sunset.text = SimpleDateFormat("hh:mm a").format(Date(sunset*1000))

                    Picasso.get().load("http://openweathermap.org/img/wn/${model.weather[0].icon}@2x.png").into(binding.logotip)

                    when {
                        model.weather[0].description.contains("rain") -> {
                            binding.weatherView.setWeatherData(PrecipType.RAIN)
                        }
                        model.weather[0].description.contains("snow") -> {
                            binding.weatherView.setWeatherData(PrecipType.SNOW)
                        }
                    }

                }
            }
        ) {
            Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
        }
        requestQueue.add(jsonObjectRequest)
    }
}