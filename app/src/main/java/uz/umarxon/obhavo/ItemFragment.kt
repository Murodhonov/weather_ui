package uz.umarxon.obhavo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.matteobattilana.weather.PrecipType
import com.squareup.picasso.Picasso
import uz.umarxon.obhavo.databinding.FragmentItemBinding
import uz.umarxon.obhavo.models.WeatherModel
import java.sql.Date
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

class ItemFragment(var model: WeatherModel) : Fragment() {

    lateinit var binding:FragmentItemBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentItemBinding.inflate(layoutInflater)

        val updatedAt:Long = model.dt.toLong()
        binding.updatedAt.text = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a").format(Date(updatedAt*1000))
        binding.address.text = model.name
        binding.status.text = model.weather[0].description
        binding.temp.text = "${model.main.temp.roundToInt()}°C"
        binding.tempMin.text  = "Min Temp: ${model.main.temp_min.roundToInt()}°C"
        binding.tempMax.text  = "Max Temp: ${model.main.temp_max.roundToInt()}°C"
        binding.pressure.text = model.main.pressure.toString()
        binding.humidity.text = model.main.humidity.toString()+"%"
        val sunrise:Long = model.sys.sunrise.toLong()
        val sunset:Long = model.sys.sunset.toLong()
        binding.sunrise.text = "${SimpleDateFormat("hh:mm a").format(Date(sunrise*1000))}"
        binding.sunset.text = "${SimpleDateFormat("hh:mm a").format(Date(sunset*1000))}"
        binding.wind.text = model.wind.speed.toString()+"k/h"

        Picasso.get().load("http://openweathermap.org/img/wn/${model.weather[0].icon}@2x.png").into(binding.logotip)

        when {
            model.weather[0].description.contains("rain") -> {
                binding.weatherView.setWeatherData(PrecipType.RAIN)
            }
            model.weather[0].description.contains("snow") -> {
                binding.weatherView.setWeatherData(PrecipType.SNOW)
            }
        }

        return binding.root
    }






}