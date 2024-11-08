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
    ): View? {
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
        // Initialize the adapter with a click listener to handle event selection
        eventAdapter = EventAdapter(mutableListOf()) { event ->
            // Handle item click: navigate to event details page using FragmentTransaction
            val eventDetailsFragment = EventDetailsFragment()

            // Pass data to the EventDetailsFragment using a Bundle
            val bundle = Bundle()
            bundle.putString("eventId", event.id) // Pass the eventId or other details
            eventDetailsFragment.arguments = bundle

            // Use FragmentTransaction to replace or add the fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, eventDetailsFragment) // Correct container ID here
                .addToBackStack(null)  // Optional: To allow back navigation
                .commit()
        }
        binding.recyclerViewEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewEvents.adapter = eventAdapter
    }



    private fun setupTabs() {
        val tabTitles = listOf("Completed", "Upcoming")
        tabTitles.forEach { title ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(title))
        }
    }

    private fun setupTabListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> loadEvents("completed")
                    1 -> loadEvents("upcoming")
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun loadEvents(status: String) {
        val currentDate = dateFormat.format(Date())

        when (status) {
            "upcoming" -> {
                db.collection("events")
                    .whereLessThan("date", currentDate)
                    .get()
                    .addOnSuccessListener { documents ->
                        val events = documents.map { document ->
                            val title = document.getString("title") ?: "Unknown Title"
                            val location = document.getString("place") ?: "Unknown Location"
                            val date = document.getString("date") ?: "Unknown Date"
                            val time = document.getString("time") ?: "Unknown Time"

                            // Log the document data for debugging purposes
                            Log.d("EventsFragment", "Event Data: Title = $title, Location = $location, Date = $date, Time = $time")

                            // Create an Event object
                            Event(
                                id = document.id,
                                title = title,
                                location = location,
                                date = date,
                                time = time
                            )
                        }
                        handleEventList(events)
                    }
                    .addOnFailureListener { exception ->
                        Log.e("EventsFragment", "Error getting documents: ", exception)
                    }
            }
            "completed" -> {
                db.collection("events")
                    .whereGreaterThan("date", currentDate)
                    .get()
                    .addOnSuccessListener { documents ->
                        val events = documents.map { document ->
                            val title = document.getString("title") ?: "Unknown Title"
                            val location = document.getString("place") ?: "Unknown Location"
                            val date = document.getString("date") ?: "Unknown Date"
                            val time = document.getString("time") ?: "Unknown Time"

                            // Log the document data for debugging purposes
                            Log.d("EventsFragment", "Event Data: Title = $title, Location = $location, Date = $date, Time = $time")

                            // Create an Event object
                            Event(
                                id = document.id,
                                title = title,
                                location = location,
                                date = date,
                                time = time
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
        // Ensure binding is not null before accessing it
        _binding?.let { safeBinding ->
            eventAdapter.updateEvents(events)

            if (events.isEmpty()) {
                safeBinding.noEventsText.visibility = View.VISIBLE
                safeBinding.recyclerViewEvents.visibility = View.GONE
            } else {
                safeBinding.noEventsText.visibility = View.GONE
                safeBinding.recyclerViewEvents.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Nullify the binding to avoid memory leaks
    }
}
