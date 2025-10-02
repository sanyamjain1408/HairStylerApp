package com.example.hairstyler

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hairstyler.databinding.ActivityCustomerSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CustomerSignupActivity : AppCompatActivity() {

    private val binding : ActivityCustomerSignupBinding by lazy {
        ActivityCustomerSignupBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Firebase init
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Already have account → Go to Login
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, CustomerLoginActivity::class.java))
            finish()
        }

        // Register Button
        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val repeatPassword = binding.etRepeatPassword.text.toString().trim()

            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            } else if (password != repeatPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val uid = auth.currentUser?.uid
                            if (uid != null) {
                                // Save user data in Firestore
                                val userMap = mapOf(
                                    "uid" to uid,
                                    "username" to username,
                                    "email" to email,
                                    "role" to "Customer"
                                )

                                db.collection("Users").document(uid)
                                    .set(userMap)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, CustomerLoginActivity::class.java))
                                        finish()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}
