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
            Event("Bruno Mars: 24K Magic World Tour", "Open Air Theatre", "Jul 25, 2024", R.drawable.event_placeholder),
            Event("Dua Lipa: Future Nostalgia Tour", "Convention Center, Main Hall", "Aug 10, 2024", R.drawable.banner),
            Event("Nothing: Ok", "Auditorium, Hall No-06", "Nov 10, 2023", R.drawable.event_placeholder)
        )
    }

    private fun getUpcomingEvents(): List<Event> {
        return listOf(
            Event("Drake: It's All A Blur Tour", "Arena, Section B", "Apr 12, 2024", R.drawable.banner),
            Event("Coldplay: Music of the Spheres", "Auditorium, Hall No-06", "Nov 15, 2023", R.drawable.event_placeholder),
            Event("Imagine Dragons: Mercury Tour", "Stadium, East Wing", "May 30, 2024", R.drawable.banner)
        )
    }

    private fun getCanceledEvents(): List<Event> {
        return listOf(
            Event("Coldplay: Canceled", "Auditorium, Hall No-06", "Nov 20, 2023", R.drawable.event_placeholder),
            Event("Taylor Swift: Canceled", "City Park, Open Air", "Jan 10, 2024", R.drawable.banner),
            Event("Coldplay: Music of the Spheres", "Auditorium, Hall No-06", "Nov 15, 2023", R.drawable.event_placeholder)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear the binding reference
    }
}
