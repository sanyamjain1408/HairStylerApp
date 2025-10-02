package com.example.hairstyler

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hairstyler.databinding.ActivityBurberloginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.firestore.FirebaseFirestore
//
import com.google.firebase.firestore.FirebaseFirestore

//import com.google.firebase.database

class BarberLoginActivity : AppCompatActivity() {
    private val binding: ActivityBurberloginBinding by lazy {
        ActivityBurberloginBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initalization Firebase auth , firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        // Check if already logged in
        val sharedPref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
        val barberIsLoggedIn = sharedPref.getBoolean("BarberisLoggedIn", false)
        if (barberIsLoggedIn) {
            startActivity(Intent(this, BarberHomePageActivity::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please Fill All The Details", Toast.LENGTH_SHORT).show()
            }
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val uid = auth.currentUser?.uid

                            if (uid!= null){
                                db.collection("Admin").document(uid).get()
                                    .addOnSuccessListener { documentSnapshot ->
                                        if (documentSnapshot.exists()){
                                            val role = documentSnapshot.getString("role")
                                            if (role == "Barber") {
                                                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                                                // Save session in SharedPreferences
                                                val editor = sharedPref.edit()
                                                editor.putBoolean("BarberisLoggedIn", true)
                                                editor.apply()

                                                startActivity(Intent(this, BarberHomePageActivity::class.java))
                                                finish()
                                            } else {
                                                auth.signOut()
                                                Toast.makeText(this, "This account is not a Barber account", Toast.LENGTH_SHORT).show()
                                            }
                                        }else {
                                            auth.signOut()
                                            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        auth.signOut()
                                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Toast.makeText(this, "LogIn Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
        }




                binding.btnSignup.setOnClickListener {
                    startActivity(Intent(this, BarberSignupActivity::class.java))
                    finish()
                }
            }

    }
