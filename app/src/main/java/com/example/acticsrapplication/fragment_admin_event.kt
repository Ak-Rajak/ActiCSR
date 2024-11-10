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

class EventsAdminFragment : Fragment() {

    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: AdminEventAdapter // Adapter for displaying events
    private val db = FirebaseFirestore.getInstance() // Firestore instance
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

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
        eventAdapter = AdminEventAdapter(mutableListOf(), { event ->
            // Navigate to EventAdminDetailsFragment with the selected eventId
            navigateToEventDetails(event.id)
        }, { event ->
            navigateToUpdateEventScreen(event)
        }, { event ->
            deleteEvent(event)
        })

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
        val currentDate = Timestamp.now()

        when (status) {
            "upcoming" -> {
                db.collection("events")
                    .whereGreaterThan("date", currentDate)
                    .get()
                    .addOnSuccessListener { documents ->
                        val events = documents.map { document ->
                            val title = document.getString("title") ?: "Unknown Title"
                            val location = document.getString("place") ?: "Unknown Location"
                            val dateTimestamp = document.getTimestamp("date")
                            val date = dateTimestamp?.toDate()?.let { dateFormat.format(it) } ?: "Unknown Date"
                            val time = dateTimestamp?.toDate()?.let { timeFormat.format(it) } ?: "Unknown Time"

                            Event(
                                id = document.id,
                                title = title,
                                location = location,
                                date = dateTimestamp ?: Timestamp.now(),
                                time = time
                            )
                        }
                        handleEventList(events)
                    }
                    .addOnFailureListener { exception ->
                        Log.e("EventsAdminFragment", "Error getting documents: ", exception)
                    }
            }
            "completed" -> {
                db.collection("events")
                    .whereLessThan("date", currentDate)
                    .get()
                    .addOnSuccessListener { documents ->
                        val events = documents.map { document ->
                            val title = document.getString("title") ?: "Unknown Title"
                            val location = document.getString("place") ?: "Unknown Location"
                            val dateTimestamp = document.getTimestamp("date")
                            val date = dateTimestamp?.toDate()?.let { dateFormat.format(it) } ?: "Unknown Date"
                            val time = dateTimestamp?.toDate()?.let { timeFormat.format(it) } ?: "Unknown Time"

                            Event(
                                id = document.id,
                                title = title,
                                location = location,
                                date = dateTimestamp ?: Timestamp.now(),
                                time = time
                            )
                        }
                        handleEventList(events)
                    }
                    .addOnFailureListener { exception ->
                        Log.e("EventsAdminFragment", "Error getting documents: ", exception)
                    }
            }
        }
    }

    private fun handleEventList(events: List<Event>) {
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

    private fun navigateToEventDetails(eventId: String) {
        val eventAdminDetailsFragment = EventAdminDetailsFragment().apply {
            arguments = Bundle().apply {
                putString("eventId", eventId)
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, eventAdminDetailsFragment) // Make sure fragment_container matches your layout
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToUpdateEventScreen(event: Event) {
        val updateEventFragment = UpdateEventFragment().apply {
            arguments = Bundle().apply {
                putString("eventId", event.id)
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, updateEventFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun deleteEvent(event: Event) {
        db.collection("events").document(event.id)
            .delete()
            .addOnSuccessListener {
                Log.d("EventsAdminFragment", "Event deleted successfully")
                loadEvents("completed")
            }
            .addOnFailureListener { e ->
                Log.e("EventsAdminFragment", "Error deleting event", e)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
