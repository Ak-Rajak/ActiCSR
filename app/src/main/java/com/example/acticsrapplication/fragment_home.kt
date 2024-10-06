package com.example.acticsrapplication

import android.os.Bundle
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

// HomeFragment
class HomeFragment : Fragment() {

    private lateinit var campusSpinner: Spinner
    private lateinit var selectedCampus: TextView
    private lateinit var recyclerViewUpcoming: RecyclerView
    private lateinit var recyclerViewInterested: RecyclerView
    private lateinit var eventsAdapterUpcoming: EventsAdapter
    private lateinit var eventsAdapterInterested: EventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Setup Spinner for Campus Selection
        campusSpinner = view.findViewById(R.id.campus_spinner)
        selectedCampus = view.findViewById(R.id.selected_campus)

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

        // Setup RecyclerViews for Upcoming and Interested Events
        recyclerViewUpcoming = view.findViewById(R.id.upcoming_events_recycler)
        recyclerViewInterested = view.findViewById(R.id.interested_events_recycler)

        // Sample data for Upcoming Events
        val upcomingEvents = listOf(
            Event("Gajajoti 2025", "Studio 44", "March 1, 2025", "05:00 PM"),
            Event("Tech Conference 2025", "Main Hall", "April 15, 2025", "10:00 AM"),
            Event("Art Exhibition", "Gallery 3", "June 5, 2025", "02:00 PM")
        )

        // Sample data for Interested Events
        val interestedEvents = listOf(
            Event("Music Fest", "Outdoor Stage", "May 10, 2025", "07:00 PM"),
            Event("Startup Workshop", "Room 201", "May 15, 2025", "09:00 AM")
        )

        // Set up the adapters and layout managers for RecyclerViews
        eventsAdapterUpcoming = EventsAdapter(upcomingEvents)
        eventsAdapterInterested = EventsAdapter(interestedEvents)

        // Horizontal scrolling for upcoming events
        recyclerViewUpcoming.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Vertical scrolling for interested events
        recyclerViewInterested.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        recyclerViewUpcoming.adapter = eventsAdapterUpcoming
        recyclerViewInterested.adapter = eventsAdapterInterested

        return view
    }
}