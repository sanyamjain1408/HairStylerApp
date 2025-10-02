package com.example.hairstyler

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hairstyler.databinding.ActivityBurbersignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BarberSignupActivity : AppCompatActivity() {

    private val binding: ActivityBurbersignupBinding by lazy {
        ActivityBurbersignupBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, BarberLoginActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {

            val email = binding.etEmail.text.toString()
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val repeatpassword = binding.etRepeatPassword.text.toString()

            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || repeatpassword.isEmpty()) {
                Toast.makeText(this, "Please Fill All the Details", Toast.LENGTH_SHORT).show()
            } else if (password != repeatpassword) {
                Toast.makeText(this, "Repeat password must be same", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid

                        // save user data in Firestore
                        val userMap = hashMapOf(
                            "uid" to uid,
                            "username" to username,
                            "email" to email,
                            "role" to "Barber"
                        )

                        db.collection("Admin").document(uid!!).set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, BarberLoginActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Registration Failed : ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
