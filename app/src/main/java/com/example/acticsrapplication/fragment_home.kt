package com.example.acticsrapplication // Replace with your app's package name

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

// Data class for Events
data class Event(val title: String, val location: String, val date: String, val time: String)

// RecyclerView Adapter for Events
class EventsAdapter(private val events: List<Event>) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.event_title)
        private val locationView: TextView = itemView.findViewById(R.id.event_location)
        private val dateView: TextView = itemView.findViewById(R.id.event_date)
        private val timeView: TextView = itemView.findViewById(R.id.event_time)

        fun bind(event: Event) {
            titleView.text = event.title
            locationView.text = event.location
            dateView.text = event.date
            timeView.text = event.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size
}

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

        recyclerViewUpcoming.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewInterested.layoutManager = LinearLayoutManager(requireContext())

        recyclerViewUpcoming.adapter = eventsAdapterUpcoming
        recyclerViewInterested.adapter = eventsAdapterInterested

        return view
    }
}
