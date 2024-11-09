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
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class EventsFragment : Fragment() {

    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter
    private val db = FirebaseFirestore.getInstance() // Initialize Firestore
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) // Format for date
    private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault()) // Format for time

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
        // Get the current date as a Timestamp
        val currentDate = Timestamp.now()

        when (status) {
            "upcoming" -> {
                db.collection("events")
                    .whereGreaterThan("date", currentDate) // Compare directly with Timestamp
                    .get()
                    .addOnSuccessListener { documents ->
                        val events = documents.map { document ->
                            val title = document.getString("title") ?: "Unknown Title"
                            val location = document.getString("place") ?: "Unknown Location"
                            val dateTimestamp = document.getTimestamp("date") // Retrieve the date as Timestamp
                            val date = dateTimestamp?.toDate()?.let { dateFormat.format(it) } ?: "Unknown Date"

                            // Retrieve the time from the timestamp
                            val time = dateTimestamp?.toDate()?.let { timeFormat.format(it) } ?: "Unknown Time"

                            // Log the document data for debugging purposes
                            Log.d("EventsFragment", "Event Data: Title = $title, Location = $location, Date = $date, Time = $time")

                            // Create an Event object with the Timestamp value
                            Event(
                                id = document.id,
                                title = title,
                                location = location,
                                date = dateTimestamp ?: Timestamp.now(), // Pass the Timestamp directly
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
                    .whereLessThan("date", currentDate) // Compare directly with Timestamp
                    .get()
                    .addOnSuccessListener { documents ->
                        val events = documents.map { document ->
                            val title = document.getString("title") ?: "Unknown Title"
                            val location = document.getString("place") ?: "Unknown Location"
                            val dateTimestamp = document.getTimestamp("date") // Retrieve the date as Timestamp
                            val date = dateTimestamp?.toDate()?.let { dateFormat.format(it) } ?: "Unknown Date"

                            // Retrieve the time from the timestamp
                            val time = dateTimestamp?.toDate()?.let { timeFormat.format(it) } ?: "Unknown Time"

                            // Log the document data for debugging purposes
                            Log.d("EventsFragment", "Event Data: Title = $title, Location = $location, Date = $date, Time = $time")

                            // Create an Event object with the Timestamp value
                            Event(
                                id = document.id,
                                title = title,
                                location = location,
                                date = dateTimestamp ?: Timestamp.now(), // Pass the Timestamp directly
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
