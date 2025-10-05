package com.example.hairstyler

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class BarberProfileFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: FloatingActionButton
    private val db = FirebaseFirestore.getInstance()
    private val barberList = mutableListOf<Barber>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_barber_profile, container, false)
        recyclerView= view.findViewById(R.id.barberRecyclerView)
        addButton = view.findViewById(R.id.btnAddBarber)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = BarberAdapter
    }

}