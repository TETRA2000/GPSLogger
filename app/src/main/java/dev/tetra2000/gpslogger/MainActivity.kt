package dev.tetra2000.gpslogger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startLocationMonitorService();
    }


    private fun startLocationMonitorService() {
        Intent(this, LocationMonitorService::class.java).also { intent ->
            applicationContext.startForegroundService(intent)
        }
    }
}