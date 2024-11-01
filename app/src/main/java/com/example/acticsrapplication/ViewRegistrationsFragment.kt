package com.example.acticsrapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewRegistrationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_registrations, container, false)

        // Sample data for demonstration
        val registrationsList = listOf(
            Registration("John Doe", "S001", "Annual Tech Conference"),
            Registration("Jane Smith", "S002", "Cultural Fest"),
            Registration("Alex Johnson", "S003", "Science Expo")
        )

        // Setup RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.registrationsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = RegistrationsAdapter(registrationsList)

        return view
    }
}
