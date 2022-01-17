package uz.umarxon.obhavo

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.eftimoff.viewpagertransformers.CubeOutTransformer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import uz.umarxon.obhavo.Data.MySharedPreference
import uz.umarxon.obhavo.adapter.MainViewPagerAdapter
import uz.umarxon.obhavo.databinding.ActivityMainBinding
import uz.umarxon.obhavo.models.WeatherModel
import com.eftimoff.viewpagertransformers.RotateUpTransformer
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.save_dialog.view.*


class MainActivity : AppCompatActivity() {

    var model: WeatherModel? = null

    lateinit var requestQueue: RequestQueue
    lateinit var binding: ActivityMainBinding
    lateinit var fusedLocatedProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestQueue = Volley.newRequestQueue(this)

        fusedLocatedProviderClient = LocationServices.getFusedLocationProviderClient(this)

        deviceLocation()
    }

    override fun onResume() {
        super.onResume()

        deviceLocation()

    }

    @SuppressLint("MissingPermission")
    fun deviceLocation() {
        val locationTask: Task<Location> = fusedLocatedProviderClient.lastLocation
        locationTask.addOnSuccessListener {
            if (it != null) {
                Log.d("Murodhonov", "deviceLocation: ${it.toString()}")

                loading("${it.latitude}", "${it.longitude}", "metric")

            }
        }
    }

    private fun loading(lat: String, lon: String, unit: String) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=6d84d89d5bf6e7b6cc1300dd9ac165f1&units=${unit}",
            null,
            { response ->
                if (response != null) {
                    model = Gson().fromJson(response.toString(), WeatherModel::class.java)
                    Log.d("Murodhonov", model.toString())

                    val list = ArrayList<WeatherModel>()

                    list.add(model!!)

                    MySharedPreference.init(this)
                    val listMain = MySharedPreference.myList

                    for (i in listMain){
                        list.add(i)
                    }

                    val sectionsPagerAdapter = MainViewPagerAdapter(binding.root.context, supportFragmentManager, list)
                    val viewPager: ViewPager = binding.viewPager
                    viewPager.adapter = sectionsPagerAdapter
                    binding.dotsIndicator.setViewPager(viewPager)
                    binding.viewPager.setPageTransformer(true, CubeOutTransformer())
                    binding.progress.visibility = View.GONE

                    binding.add.setOnClickListener {
                        startActivity(Intent(this,MapsActivity::class.java))
                    }

                }
            }
        ) {
            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
        }
        requestQueue.add(jsonObjectRequest)
    }
}

