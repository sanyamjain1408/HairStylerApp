package com.example.hairstyler

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private val splashDuration: Long = 3000 // 3 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Handler to delay for splashDuration
        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
            val customerIsLoggedIn = sharedPref.getBoolean("CustomerisLoggedIn", false)
            val barberIsLoggedIn = sharedPref.getBoolean("BarberisLoggedIn", false)

            when {
                customerIsLoggedIn -> {
                    // Customer logged in → go to CustomerHome
                    startActivity(Intent(this, CustomerHomeActivity::class.java))
                }
                barberIsLoggedIn -> {
                    // Barber logged in → go to BarberHome
                    startActivity(Intent(this, BarberHomePageActivity::class.java))
                }
                else -> {
                    // No one logged in → go to Startpage
                    startActivity(Intent(this, StartpageActivity::class.java))
                }
            }

            finish() // close splash activity
            },splashDuration)
    }
}
