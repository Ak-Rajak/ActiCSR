package com.example.acticsrapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.acticsrapplication.databinding.FragmentEventBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventsFragment : Fragment() {

    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter
    private val db = FirebaseFirestore.getInstance() // Initialize Firestore
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) // Date format used in Firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabs()
        setupRecyclerView()
        setupTabListener()

        // Load completed events by default
        loadEvents("completed")
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter(mutableListOf()) // Start with an empty list
        binding.recyclerViewEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewEvents.adapter = eventAdapter
    }

    private fun setupTabs() {
        // Only two tabs: Completed and Upcoming
        val tabTitles = listOf("Completed", "Upcoming")
        tabTitles.forEach { title ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(title))
        }
    }

    private fun setupTabListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> loadEvents("completed")  // Fetch completed events
                    1 -> loadEvents("upcoming")   // Fetch upcoming events
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    // Fetch events from Firestore based on their category (completed, upcoming)
    private fun loadEvents(status: String) {
        val currentDate = Date()
        val formattedCurrentDate = dateFormat.format(currentDate)

        when (status) {
            "upcoming" -> {
                // Fetch events with dates in the future
                db.collection("events")
                    .whereLessThan("date", formattedCurrentDate) // Changed to fetch future events
                    .get()
                    .addOnSuccessListener { documents ->
                        val events = documents.map { document ->
                            Event(
                                title = document.getString("title") ?: "Unknown Title",
                                location = document.getString("location") ?: "Unknown Location",
                                date = document.getString("date") ?: "Unknown Date",
                                time = document.getString("time") ?: "Unknown Time"
                            )
                        }
                        handleEventList(events)
                    }
                    .addOnFailureListener { exception ->
                        Log.e("EventsFragment", "Error getting documents: ", exception)
                    }
            }
            "completed" -> {
                // Fetch events with dates in the past
                db.collection("events")
                    .whereGreaterThan("date", formattedCurrentDate) // Changed to fetch past events
                    .get()
                    .addOnSuccessListener { documents ->
                        val events = documents.map { document ->
                            Event(
                                title = document.getString("title") ?: "Unknown Title",
                                location = document.getString("location") ?: "Unknown Location",
                                date = document.getString("date") ?: "Unknown Date",
                                time = document.getString("time") ?: "Unknown Time"
                            )
                        }
                        handleEventList(events)
                    }
                    .addOnFailureListener { exception ->
                        Log.e("EventsFragment", "Error getting documents: ", exception)
                    }
            }
        }
    }

    // Handle the event list and show/hide the "No events" message
    private fun handleEventList(events: List<Event>) {
        eventAdapter.updateEvents(events)

        if (events.isEmpty()) {
            binding.noEventsText.visibility = View.VISIBLE
            binding.recyclerViewEvents.visibility = View.GONE
        } else {
            binding.noEventsText.visibility = View.GONE
            binding.recyclerViewEvents.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear the binding reference to avoid memory leaks
    }
}
