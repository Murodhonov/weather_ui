package uz.umarxon.obhavo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.save_dialog.view.*
import uz.umarxon.obhavo.Data.MySharedPreference
import uz.umarxon.obhavo.databinding.ActivityMapsBinding
import uz.umarxon.obhavo.models.WeatherModel
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocatedProviderClient: FusedLocationProviderClient
    private var model: WeatherModel? = null
    private lateinit var requestQueue: RequestQueue
    private var MY_API_KEY = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestQueue = Volley.newRequestQueue(this)

        fusedLocatedProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission", "InflateParams")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val locationTask: Task<Location> = fusedLocatedProviderClient.lastLocation
        locationTask.addOnSuccessListener { it ->
            if (it != null) {
                Log.d("Murodhonov", "deviceLocation: $it")

                val location1 = LatLng(it.latitude, it.longitude)
                mMap.addMarker(
                    MarkerOptions().position(location1).title(
                        "Marker in ${getAddressFromLatLng(this, location1)}"
                    )
                )
                mMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(it.latitude, it.longitude),
                        20.0f
                    )
                )

                mMap.setOnMapLongClickListener { map2 ->

                    val d = BottomSheetDialog(this)

                    val i = LayoutInflater.from(this).inflate(
                        R.layout.save_dialog, null, false
                    )

                    d.setContentView(i)

                    var a = getAddressFromLatLng(this, LatLng(map2.latitude, map2.longitude))

                    if (a != null) {
                        if (a.isEmpty()){
                            a = "${ map2.latitude } ${map2.longitude}"
                        }
                    }
                    i.adress.text = a

                    i.yuq.setOnClickListener {
                        d.hide()
                    }
                    i.ha.setOnClickListener {
                        d.hide()

                        loading(map2.latitude, map2.longitude, "metric")
                    }

                    d.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                    d.setCancelable(true)

                    d.show()

                }
                mMap.setOnMapClickListener {
                    Toast.makeText(this, "${it.latitude} \n ${it.longitude}", Toast.LENGTH_SHORT)
                        .show()
                }

                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            }
        }
    }

    private fun getAddressFromLatLng(context: Context?, latLng: LatLng): String? {
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(context, Locale.getDefault())
        return try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            addresses[0].getAddressLine(0)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun loading(lat: Double, lon: Double, unit: String) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${MY_API_KEY}&units=${unit}",
            null,
            { response ->
                if (response != null) {
                    model = Gson().fromJson(response.toString(), WeatherModel::class.java)

                    MySharedPreference.init(this)
                    val list = MySharedPreference.myList
                    list.add(model!!)
                    MySharedPreference.myList = list

                    finish()
                }
            }
        ) {
            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
        }
        requestQueue.add(jsonObjectRequest)
    }

}