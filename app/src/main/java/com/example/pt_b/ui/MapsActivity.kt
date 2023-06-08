package com.example.pt_b.ui
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.pt_b.R
import com.google.android.gms.maps.OnMapReadyCallback
import com.example.pt_b.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var convName: String
    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var conv: Location? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 이전 액티비티에서 편의점 이름 받아오기
        val intent = intent
        if (intent != null && intent.hasExtra("location")) {
            convName = intent.getStringExtra("location") ?: ""
        } else {
            convName = ""
        }
        //권한
        val pemissions = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        onRequestPermissionResult(100, pemissions, intArrayOf())
        requirePermission(pemissions, 100)


    }

    override fun onMapReady(googlemap: GoogleMap) {
        mMap = googlemap
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        updateLocation()

    }

    @SuppressWarnings("MissingPermission")
    fun updateLocation(){
        val locationRequest = LocationRequest.create()
        locationRequest.run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult?.let {
                    for(location in it.locations) {
                        lastLocation(location)
                    }
                super.onLocationResult(locationResult)
            }
        }

    }
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }
    fun lastLocation(lastLocation: Location) {
        val myLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
        val markerOptions = MarkerOptions()
            .position(myLocation)
            .title("현재위치")
        val cameraPosition = CameraPosition.builder()
            .target(myLocation)
            .zoom(18f)
            .build()
        mMap.clear()
        mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    fun stratMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    private fun requirePermission(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionGranted(requestCode)
        } else{
            ActivityCompat.requestPermissions(this, permissions, requestCode)
        }
    }

    fun onRequestPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            permissionGranted(requestCode)
        } else {
            permissionDenied(requestCode)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    fun permissionGranted(requestCode: Int) {
        stratMap()
    }
    fun permissionDenied(requestCode: Int) {
        Toast.makeText(this, "권한이 승인되야 실행가능합니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            permissionGranted(requestCode)
        } else {
            permissionDenied(requestCode)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}