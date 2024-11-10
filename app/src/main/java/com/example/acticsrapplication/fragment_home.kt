package com.example.acticsrapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.acticsrapplication.databinding.FragmentHomeBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var recyclerViewUpcoming: RecyclerView
    private lateinit var eventsAdapterUpcoming: EventsAdapter
    private lateinit var recyclerViewRecentActivity: RecyclerView
    private lateinit var eventsAdapterRecentActivity: EventsAdapter
    private val db = FirebaseFirestore.getInstance() // Firestore instance

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        // Setup RecyclerView for Upcoming Events
        recyclerViewUpcoming = binding.upcomingEventsRecycler
        eventsAdapterUpcoming = EventsAdapter(mutableListOf()) { event ->
            navigateToEventRegistration(event.id)
        }

        // Horizontal scrolling for upcoming events
        recyclerViewUpcoming.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewUpcoming.adapter = eventsAdapterUpcoming

        // Setup RecyclerView for Your Registrations
        recyclerViewRecentActivity = binding.recyclerRegistrationActivity
        eventsAdapterRecentActivity = EventsAdapter(mutableListOf()) { event ->
            navigateToEventRegistration(event.id)
        }

        // Vertical scrolling for recent events
        recyclerViewRecentActivity.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewRecentActivity.adapter = eventsAdapterRecentActivity

        // Load upcoming events and recent activity from Firestore
        loadUpcomingEvents()
        loadRecentActivity()

        return binding.root
    }

    private fun loadUpcomingEvents() {
        db.collection("events")
            .whereGreaterThan("date", Timestamp.now()) // Use Timestamp to filter events after the current date
            .orderBy("date", Query.Direction.ASCENDING) // Order events by date
            .get()
            .addOnSuccessListener { documents ->
                val upcomingEvents = documents.map { document ->
                    val timestamp = document.getTimestamp("date") ?: Timestamp.now()
                    val date = timestamp.toDate()
                    Event(
                        id = document.id,
                        title = document.getString("title") ?: "Unknown Title",
                        location = document.getString("place") ?: "Unknown Location",
                        date = timestamp,  // Store the Timestamp directly
                        time = document.getString("time") ?: "Unknown Time"
                    )
                }
                eventsAdapterUpcoming.updateEvents(upcomingEvents)
            }
            .addOnFailureListener { exception ->
                Log.e("HomeFragment", "Error getting upcoming events: ", exception)
            }
    }

    private fun loadRecentActivity() {
        db.collection("events")
            .whereLessThanOrEqualTo("date", Timestamp.now()) // Fetch events that are before or on the current date
            .orderBy("date", Query.Direction.DESCENDING) // Order by most recent
            .limit(5) // Fetch only the latest 5 events
            .get()
            .addOnSuccessListener { documents ->
                val recentEvents = documents.map { document ->
                    val timestamp = document.getTimestamp("date") ?: Timestamp.now()
                    val date = timestamp.toDate()
                    Event(
                        id = document.id,
                        title = document.getString("title") ?: "Unknown Title",
                        location = document.getString("place") ?: "Unknown Location",
                        date = timestamp,
                        time = document.getString("time") ?: "Unknown Time"
                    )
                }
                eventsAdapterRecentActivity.updateEvents(recentEvents)
            }
            .addOnFailureListener { exception ->
                Log.e("HomeFragment", "Error getting recent events: ", exception)
            }
    }

    // Helper function to convert Timestamp to formatted string
    private fun formatDate(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    private fun navigateToEventRegistration(eventId: String) {
        val eventDetailsFragment = EventDetailsFragment() // Assuming this fragment exists
        val args = Bundle().apply {
            putString("eventId", eventId) // Passing the event ID as an argument
        }
        eventDetailsFragment.arguments = args

        // Perform the fragment transaction to navigate to EventRegistrationFragment
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, eventDetailsFragment) // Adjust container ID if needed
            .addToBackStack(null) // Add to back stack to allow "back" navigation
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}