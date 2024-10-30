package com.example.acticsrapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.acticsrapplication.databinding.FragmentEventBinding
import com.google.android.material.tabs.TabLayout

class EventsFragment : Fragment() {

    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using view binding
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup TabLayout with tabs
        setupTabs()

        // Setup RecyclerView
        val eventAdapter = EventAdapter(getUpcomingEvents().toMutableList()) // Use upcoming events by default
        binding.recyclerViewEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewEvents.adapter = eventAdapter

        // Setup TabLayout listener
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> eventAdapter.updateEvents(getCompletedEvents())  // Completed Events
                    1 -> eventAdapter.updateEvents(getUpcomingEvents())   // Upcoming Events
                    2 -> eventAdapter.updateEvents(getCanceledEvents())   // Canceled Events
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

            addTab(completedTab)
            addTab(upcomingTab)
            addTab(canceledTab)
        }
    }

    private fun getCompletedEvents(): List<Event> {
        return listOf(
            Event("Code for Change", "Innovation Lab, Portland, OR", "September 15, 2024", R.drawable.event_placeholder, "10:00 AM"),
            Event("Hackathon Heroes", "City Hall Community Room, Austin, TX", "March 23-24, 2024", R.drawable.event_placeholder, "09:00 AM"),
            Event("Music Fest", "University Auditorium, Seattle, WA", "May 18, 2024", R.drawable.andimg4, "07:00 PM"),
            Event("Tech Innovators", "Grand Hotel, Chicago, IL", "June 7-8, 2024", R.drawable.event_placeholder, "11:00 AM"),
            Event("Summer Code Sprint", "Community Tech Hub, Boston, MA", "July 14, 2024", R.drawable.event_placeholder, "12:00 PM")
        )
    }

    private fun getUpcomingEvents(): List<Event> {
        return listOf(
            Event("CodeFest", "Tech Convention Center, Hall No-6", "February 10, 2025", R.drawable.andimg6, "02:00 PM"),
            Event("Hackathon'25", "Innovation Hub, Koutilya Building", "January 25, 2025", R.drawable.andimg7, "11:00 AM"),
            Event("Neon Evening Party", "Downtown Club", "December 15, 2025", R.drawable.andimg1, "06:00 PM"),
            Event("DebugFest", "Tech Arena, USA", "January 12, 2025", R.drawable.andimg8, "10:00 AM"),
            Event("Summer Splash Bash", "The Grand Ballroom, R-10", "March 3, 2025", R.drawable.andimg5, "05:00 PM")
        )
    }

    private fun getCanceledEvents(): List<Event> {
        return listOf(
            Event("Coldplay: Canceled", "Auditorium, Hall No-06", "Nov 20, 2023", R.drawable.banner, "08:00 PM"),
            Event("Music Wave Festival", "City Park, Open Air", "Jan 10, 2024", R.drawable.andimg3, "05:00 PM"),
            Event("Code Sprint 2024", "Digital Labs, AB Building", "December 5, 2024", R.drawable.andimg9, "09:00 AM"),
            Event("ColdPlay, Music of the Spheres", "Garden Area, MD Building Front", "June 22, 2024", R.drawable.andimg2, "07:00 PM"),
            Event("DevCon'2024", "Expo Center, Bhubaneswar", "April 15, 2025", R.drawable.andimg10, "10:00 AM")
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear the binding reference
    }
}