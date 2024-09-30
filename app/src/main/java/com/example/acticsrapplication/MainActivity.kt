package com.example.acticsrapplication // Replace with your app's package name

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class Event(val title: String, val location: String, val date: String, val time: String)

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

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewUpcoming: RecyclerView
    private lateinit var recyclerViewInterested: RecyclerView
    private lateinit var eventsAdapterUpcoming: EventsAdapter
    private lateinit var eventsAdapterInterested: EventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Your activity layout

        recyclerViewUpcoming = findViewById(R.id.upcoming_events_recycler)
        recyclerViewInterested = findViewById(R.id.interested_events_recycler)

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

        // Set up the adapters and layout managers
        eventsAdapterUpcoming = EventsAdapter(upcomingEvents)
        eventsAdapterInterested = EventsAdapter(interestedEvents)

        recyclerViewUpcoming.layoutManager = LinearLayoutManager(this)
        recyclerViewInterested.layoutManager = LinearLayoutManager(this)

        recyclerViewUpcoming.adapter = eventsAdapterUpcoming
        recyclerViewInterested.adapter = eventsAdapterInterested
    }
}
