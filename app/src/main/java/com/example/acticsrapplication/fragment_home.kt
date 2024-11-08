package com.example.acticsrapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.acticsrapplication.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var campusSpinner: Spinner
    private lateinit var selectedCampus: TextView
    private lateinit var recyclerViewUpcoming: RecyclerView
    private lateinit var eventsAdapterUpcoming: EventsAdapter
    private val db = FirebaseFirestore.getInstance() // Firestore instance

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Setup Spinner for Campus Selection
        campusSpinner = binding.campusSpinner
        selectedCampus = binding.selectedCampus

        // Setup Spinner Adapter with the campus list
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.campus_list,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            campusSpinner.adapter = adapter
        }

        // Handle Spinner Selection
        campusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItemView = view as TextView
                selectedItemView.setTextColor(resources.getColor(R.color.midnight_blue))
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        // Setup RecyclerView for Upcoming Events
        recyclerViewUpcoming = binding.upcomingEventsRecycler
        eventsAdapterUpcoming = EventsAdapter(mutableListOf())

        // Horizontal scrolling for upcoming events
        recyclerViewUpcoming.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewUpcoming.adapter = eventsAdapterUpcoming

        // Load upcoming events from Firestore
        loadUpcomingEvents()

        return binding.root
    }

    private fun loadUpcomingEvents() {
        db.collection("events")
            .whereLessThan("date", getCurrentDate()) // Assumes `date` field is a string in "MMM dd, yyyy" format
            .get()
            .addOnSuccessListener { documents ->
                val upcomingEvents = documents.map { document ->
                    Event(
                        id = document.id,
                        title = document.getString("title") ?: "Unknown Title",
                        location = document.getString("place") ?: "Unknown Location",
                        date = document.getString("date") ?: "Unknown Date",
                        time = document.getString("time") ?: "Unknown Time"
                    )
                }
                eventsAdapterUpcoming.updateEvents(upcomingEvents)
            }
            .addOnFailureListener { exception ->
                Log.e("HomeFragment", "Error getting upcoming events: ", exception)
            }
    }

    // Helper function to get the current date in the required format
    private fun getCurrentDate(): String {
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return formatter.format(Date())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
