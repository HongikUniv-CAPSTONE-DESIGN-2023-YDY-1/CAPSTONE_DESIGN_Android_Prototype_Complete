package com.example.pt_b

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.pt_b.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var searchLocation: String
    private lateinit var placesClient: PlacesClient
    private val permissionLancher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            onMapReady(mMap)
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        if (intent !=null && intent.hasExtra("loaction")){
            searchLocation = intent.getStringExtra("location") ?: ""
        }
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_maps_key))
            placesClient = Places.createClient(this)
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
        val isPermissionGranted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        if (!isPermissionGranted) {
            permissionLancher.launch(permission)
        }
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {location: Location? ->
                if (location != null) {
                    searchNearbyCon(location)
                } else {
                    Toast.makeText(this, "Location is null", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
        }
    }
    private fun searchNearbyCon(currentLocation: Location) {
        val request = FindCurrentPlaceRequest.builder(listOf(Place.Field.NAME, Place.Field.LAT_LNG)).build()

        placesClient.findCurrentPlace(request)
            .addOnSuccessListener { response: FindCurrentPlaceResponse ->
                val places = response.placeLikelihoods
                if (places.isNotEmpty()) {
                    for (placeLikelihood in places) {
                        val place = placeLikelihood.place
                        val placeLocation = place.latLng
                        if (placeLocation !=null) {
                            val markerOptions = MarkerOptions()
                                .position(placeLocation)
                                .title(place.name)
                            mMap.addMarker(markerOptions)
                        }
                    }
                    val firstPlace = places[0].place
                    val firstPlaceLocation = firstPlace.latLng
                    if (firstPlaceLocation != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstPlaceLocation, 18f))
                    }
                } else {
                    Toast.makeText(this, "Place not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnSuccessListener { exception ->
                Toast.makeText(this, "오류", Toast.LENGTH_SHORT).show()
            }
    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }

}