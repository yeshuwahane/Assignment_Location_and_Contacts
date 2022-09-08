package com.alien.location

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.alien.location.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var tvLatitude: TextView
    private lateinit var tvLongitude: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        tvLatitude = findViewById(R.id.tv_latitude)
        tvLongitude = findViewById(R.id.tv_longitude)

        getCurrentLocation()

        binding.btnContacts.setOnClickListener {
            val intent = Intent(this,ContactsActivity::class.java)
            startActivity(intent)
        }

    }

    fun getCurrentLocation(){

        if (checkPermission()){
            if (isLocationEnabled())
            {
                //getting longitude and latitude

                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this){ task ->
                    val location: Location? = task.result
                    if (location==null){
                        Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "successfully location captured", Toast.LENGTH_SHORT).show()

                        Log.d("alien","latitude = ${location.latitude} and longitude = ${location.longitude}")
                        tvLongitude.text =""+ location.longitude
                        tvLatitude.text = ""+location.latitude


                    }


                }


            }else{

                Toast.makeText(this, "Turn on Location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
        else{
            //request permission
            requestPermission()
        }

    }


    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        == PackageManager.PERMISSION_GRANTED
            &&
                    ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    )
            == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }
        else{
            return false
        }
    }


    private fun requestPermission(){

        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_ACCESS_LOCATION
        )

    }



    fun isLocationEnabled(): Boolean {

        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager // typecasting

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    }

    companion object{
        private const val REQUEST_ACCESS_LOCATION = 100
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_ACCESS_LOCATION){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "Granted location", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            }
            else{
                Toast.makeText(applicationContext, "Location not granted", Toast.LENGTH_SHORT).show()
            }
        }

    }

}