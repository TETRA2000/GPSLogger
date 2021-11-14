package dev.tetra2000.gpslogger

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initLocationPermissionRequest()
    }

    fun activateLocationMonitoring(view: View) {
        if (hasAllLocationPermission()) {
            startLocationMonitorService()
        } else {
            requestLocationPermission()
        }
    }


    private fun startLocationMonitorService() {
        // TODO: Check whether the service has already started

        Intent(this, LocationMonitorService::class.java).also { intent ->
            applicationContext.startForegroundService(intent)
        }
    }

    private fun requestLocationPermission() {
        // TODO: request `ACCESS_FINE_LOCATION` and `ACCESS_BACKGROUND_LOCATION` separately
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            )
        )
    }

    private fun initLocationPermissionRequest() {
        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // TODO: prevent infinite loop when request denied
                    if(!hasAllLocationPermission()) {
                        requestLocationPermission()
                    }
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // TODO: prevent infinite loop when request denied
                    if(!hasAllLocationPermission()) {
                        requestLocationPermission()
                    }
                }
                else -> {
                    // No location access granted.
                }
            }
        }
    }

    private fun hasAllLocationPermission(): Boolean {
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

}