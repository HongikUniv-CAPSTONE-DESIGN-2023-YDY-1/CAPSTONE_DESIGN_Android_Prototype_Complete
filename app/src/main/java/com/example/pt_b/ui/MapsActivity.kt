package com.example.pt_b.ui

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat

import com.example.pt_b.R
import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.pt_b.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlin.math.pow
import kotlin.math.sqrt

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient
    private lateinit var placeName: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        placeName = intent.getStringExtra("placeName").toString().trim()
        if (placeName == "CU") {
            placeName = "CU"
        } else if (placeName == "GS25") {
            placeName = "GS25"
        } else if (placeName == "SEVENELEVEN") {
            placeName = "세븐일레븐"
        } else if (placeName == "EMART24") {
            placeName = "이마트24"
        } else {
            placeName = "편의점"
        }
        Log.d("placeName", placeName)
        Places.initialize(applicationContext, getString(R.string.google_maps_key))

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient = FusedLocationProviderClient(this)
        placesClient = Places.createClient(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()
        fetchCurrentLocation()
    }

    private fun fetchCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true

            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))
                    mMap.addMarker(MarkerOptions().position(currentLatLng).title("현재 위치"))

                    searchPlaces(currentLatLng)
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        }
    }

    private fun searchPlaces(currentLatLng: LatLng) {
        try {


            val request = FindCurrentPlaceRequest.builder(listOf(Place.Field.NAME, Place.Field.LAT_LNG))

                .build()

            placesClient.findCurrentPlace(request).addOnSuccessListener { response ->
                mMap.clear()
                var markerAdded = false
                for (placeLikelihood in response.placeLikelihoods) {
                    val place =placeLikelihood.place
                    val placeLatLng = place.latLng
                    if (placeLatLng != null &&place.name?.startsWith(placeName) == true) {
                        mMap.addMarker(MarkerOptions().position(placeLatLng).title(place.name))
                        markerAdded = true
                    }
                }
                if (!markerAdded) {

                    Toast.makeText(this, "주변에 ${placeName}이(가) 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "오류 발생: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: SecurityException) {
            Toast.makeText(this, "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchCurrentLocation()
            } else {
                Toast.makeText(this, "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }



}