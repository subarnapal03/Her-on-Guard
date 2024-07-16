package com.example.myapplication
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    fun shareLocation(view: View) {
        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        // Get the user's location
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude

                    // Create a Google Maps link with the user's location
                    val mapUrl = "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"


                    // Replace 'SAMPLE_PHONE_NUMBER' with the sample phone number you want to send the link to
                    val phoneNumber1 = "7595992745"
                    val phoneNumber2 = "9474504305"
                    val phoneNumber3 = "9932218374"

                    // Send the location link via SMS
                    sendLocationSms(phoneNumber1, mapUrl)
                    sendLocationSms(phoneNumber2, mapUrl)
                    sendLocationSms(phoneNumber3, mapUrl)
                } ?: run {
                    Toast.makeText(this, "Location not available.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendLocationSms(phoneNumber: String, mapUrl: String) {
        val smsManager = SmsManager.getDefault()
        val message = "Emergency : I need help!!! My location is: $mapUrl"

        try {
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(this, "Location link sent via SMS.", Toast.LENGTH_SHORT).show()
        } catch (ex: Exception) {
            Toast.makeText(this, "Failed to send location link via SMS.", Toast.LENGTH_SHORT).show()
            ex.printStackTrace()
        }
    }

    fun openChatbot(view: View) {
        val intent = Intent(this, ChatbotActivity::class.java)
        startActivity(intent)
    }
}
