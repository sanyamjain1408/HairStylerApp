package com.example.hairstyler

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hairstyler.databinding.ActivityCustomerLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class CustomerLoginActivity : AppCompatActivity() {
    private val binding: ActivityCustomerLoginBinding by lazy {
        ActivityCustomerLoginBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initalization Firebase auth
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Check if already logged in
        val sharedPref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
        val customerIsLoggedIn = sharedPref.getBoolean("CustomerisLoggedIn", false)
        if (customerIsLoggedIn) {
            startActivity(Intent(this, CustomerHomeActivity::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please Fill All The Details", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val uid = auth.currentUser?.uid
                            if (uid != null) {
                                db.collection("Users").document(uid).get()
                                    .addOnSuccessListener { documentSnapshot ->
                                        if (documentSnapshot.exists()) {
                                            val role = documentSnapshot.getString("role")
                                            if (role == "Customer") {
                                                Toast.makeText(
                                                    this,
                                                    "LoginIn Successful",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                                //  Save session in SharedPreferences
                                                val editor = sharedPref.edit()
                                                editor.putBoolean("CustomerisLoggedIn", true)
                                                editor.apply()

                                                startActivity(
                                                    Intent(this, CustomerHomeActivity::class.java)
                                                )
                                                finish()
                                            } else {
                                                auth.signOut()
                                                Toast.makeText(
                                                    this,
                                                    "This account is not a Customer account",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        } else {
                                            auth.signOut()
                                            Toast.makeText(
                                                this,
                                                "User data not found",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        auth.signOut()
                                        Toast.makeText(
                                            this,
                                            "Error: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        } else {
                            Toast.makeText(
                                this,
                                "LogIn Failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

            binding.btnSignup.setOnClickListener {
                startActivity(Intent(this, CustomerSignupActivity::class.java))
                finish()
            }
        }
    }
