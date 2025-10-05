package com.example.hairstyler

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class AddBarberFragment : Fragment() {

    private lateinit var name: EditText
    private lateinit var expertise: EditText
    private lateinit var addbtn: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_barber, container, false)

        name= view.findViewById(R.id.Name)
        expertise = view.findViewById(R.id.Expertise)
        addbtn = view.findViewById(R.id.btnSubmit)

        addbtn.setOnClickListener {
            saveBarberToFirestore()
        }

        return view
    }

    private fun saveBarberToFirestore() {
        val barber = hashMapOf(
            "name" to name.text.toString(),
            "expertise" to expertise.text.toString(),
            "imageUrl" to ""
        )

        db.collection("barbers").add(barber)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Barber Added!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack() // goes back to previous screen
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error adding barber", Toast.LENGTH_SHORT).show()
            }
    }
}
