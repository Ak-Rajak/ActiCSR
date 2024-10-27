package com.example.acticsrapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.acticsrapplication.databinding.ActivityEventsBinding
import com.google.android.material.tabs.TabLayout

class EventActivitys : AppCompatActivity() {

    private lateinit var binding: ActivityEventsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use view binding to inflate the layout
        binding = ActivityEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup TabLayout with tabs
        setupTabs()

        // Setup RecyclerView
        val eventAdapter = EventAdapter(getUpcomingEvents().toMutableList()) // Use upcoming events by default
        binding.recyclerViewEvents.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewEvents.adapter = eventAdapter

        // Setup TabLayout listener
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> eventAdapter.updateEvents(getCompletedEvents())
                    1 -> eventAdapter.updateEvents(getUpcomingEvents())
                    2 -> eventAdapter.updateEvents(getCanceledEvents())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    // Method to set up tabs
    private fun setupTabs() {
        binding.tabLayout.apply {
            val completedTab = newTab().setText("Completed")
            val upcomingTab = newTab().setText("Upcoming")
            val canceledTab = newTab().setText("Canceled")

            completedTab.contentDescription = "Completed Events"
            upcomingTab.contentDescription = "Upcoming Events"
            canceledTab.contentDescription = "Canceled Events"

            addTab(completedTab)
            addTab(upcomingTab)
            addTab(canceledTab)
        }
    }

    private fun getCompletedEvents(): List<Event> {
        return listOf(
            Event("Code for Change", "Innovation Lab, Portland, OR", "September 15, 2024", R.drawable.event_placeholder),
            Event("Hackathon Heroes", "City Hall Community Room, Austin, TX", "March 23-24, 2024", R.drawable.event_placeholder),
            Event("Music Fest", "University Auditorium, Seattle, WA", "May 18, 2024", R.drawable.andimg4),
            Event("Tech Innovators", "Grand Hotel, Chicago, IL", "June 7-8, 2024", R.drawable.event_placeholder),
            Event("Summer Code Sprint", "Community Tech Hub, Boston, MA", "July 14, 2024", R.drawable.event_placeholder)
        )
    }

    private fun getUpcomingEvents(): List<Event> {
        return listOf(
            Event("CodeFest", "Tech Convention Center, Hall No-6", "February 10, 2025", R.drawable.andimg6),
            Event("Hackathon'25", "Innovation Hub, Koutilya Building", "January 25, 2025", R.drawable.andimg7),
            Event("Neon Evening Party", "Downtown Club", "December 15, 2025", R.drawable.andimg1),
            Event("DebugFest", "Tech Arena, USA", "January 12, 2025", R.drawable.andimg8),
            Event("Summer Splash Bash", "The Grand Ballroom, R-10", "March 3, 2025", R.drawable.andimg5)
        )
    }

    private fun getCanceledEvents(): List<Event> {
        return listOf(
            Event("Coldplay: Canceled", "Auditorium, Hall No-06", "Nov 20, 2023", R.drawable.banner),
            Event("Music Wave Festival", "City Park, Open Air", "Jan 10, 2024", R.drawable.andimg3),
            Event("Code Sprint 2024", "Digital Labs, AB Building", "December 5, 2024", R.drawable.andimg9),
            Event("ColdPlay, Music of the Spheres", "Garden Area, MD Building Front", "June 22, 2024", R.drawable.andimg2),
            Event("DevCon'2024", "Expo Center, Bhubaneswar", "April 15, 2025", R.drawable.andimg10)
        )
    }

}
