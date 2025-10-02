package com.example.hairstyler

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hairstyler.databinding.ActivityStartpageBinding

class StartpageActivity : AppCompatActivity() {
    private val binding : ActivityStartpageBinding by lazy {
        ActivityStartpageBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.customer.setOnClickListener {
            val sharedPref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
            val customerisLoggedIn = sharedPref.getBoolean("CustomerisLoggedIn", false)
            if (customerisLoggedIn){
                val intent = Intent(this, CustomerHomeActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this, CustomerSignupActivity::class.java)
                startActivity(intent)
            }
        }

        binding.barber.setOnClickListener {

            val sharedPref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
            val barberisLoggedIn = sharedPref.getBoolean("BarberisLoggedIn",false)

            if (barberisLoggedIn){
                val intent = Intent(this, BarberHomePageActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this, BarberSignupActivity::class.java)
                startActivity(intent)
            }

        }

    }
}