package com.example.hairstyler

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.hairstyler.databinding.ActivityBarberhomepageBinding
import com.google.firebase.auth.FirebaseAuth

class BarberHomePageActivity : AppCompatActivity() {
    private val binding : ActivityBarberhomepageBinding by lazy {
        ActivityBarberhomepageBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.logout.setOnClickListener {

            //  Extra code: Clear login session
            val sharedPref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.clear()
            editor.apply()

            auth.signOut()
            Toast.makeText(this, "Log-Out", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, StartpageActivity::class.java))
            finish()
        }


        }
    }