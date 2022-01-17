package uz.umarxon.obhavo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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


class MainActivity : AppCompatActivity() {

    var model: WeatherModel? = null

    lateinit var requestQueue: RequestQueue
    lateinit var binding: ActivityMainBinding
    lateinit var fusedLocatedProviderClient: FusedLocationProviderClient
    var MY_API_KEY = "6d84d89d5bf6e7b6cc1300dd9ac165f1"//Replace with your openweather api key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.alphaContent.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim))
        Handler().postDelayed({
            binding.progress.visibility = View.GONE
        },3000)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION,)
            } else {
                TODO("VERSION.SDK_INT < Q")
            }
            ActivityCompat.requestPermissions(this, permissions, 0)
        }else{
            requestQueue = Volley.newRequestQueue(this)

            fusedLocatedProviderClient = LocationServices.getFusedLocationProviderClient(this)

            deviceLocation()
        }
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
            "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${MY_API_KEY}&units=${unit}",
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

                    val sectionsPagerAdapter = MainViewPagerAdapter(supportFragmentManager, list)
                    val viewPager: ViewPager = binding.viewPager
                    viewPager.adapter = sectionsPagerAdapter
                    binding.dotsIndicator.setViewPager(viewPager)
                    binding.viewPager.setPageTransformer(true, CubeOutTransformer())

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

