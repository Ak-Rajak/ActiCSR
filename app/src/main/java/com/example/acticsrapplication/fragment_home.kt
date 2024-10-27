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
import com.example.acticsrapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var campusSpinner: Spinner
    private lateinit var selectedCampus: TextView
    private lateinit var recyclerViewUpcoming: RecyclerView
    private lateinit var recyclerViewInterested: RecyclerView
    private lateinit var eventsAdapterUpcoming: EventsAdapter
    private lateinit var eventsAdapterInterested: EventsAdapter

    // Sample category data
    private val categoryList = listOf(
        Category(R.drawable.category1, "Category 1"),
        Category(R.drawable.category2, "Category 2"),
        Category(R.drawable.category3, "Category 3"),
        Category(R.drawable.category4, "Category 4"),
        Category(R.drawable.category5, "Category 5")
    )

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Setup RecyclerView for categories (horizontal scrolling)
        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.categoryRecyclerView.adapter = CategoryAdapter(categoryList)

        // Setup Spinner for Campus Selection
        campusSpinner = binding.campusSpinner
        selectedCampus = binding.selectedCampus

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
        recyclerViewUpcoming = binding.upcomingEventsRecycler
        recyclerViewInterested = binding.interestedEventsRecycler

        // Sample data for Upcoming Events
        val upcomingEvents = listOf(
            Events("Gajajoti 2025", "Studio 44", "March 1, 2025", "05:00 PM"),
            Events("Tech Conference 2025", "Main Hall", "April 15, 2025", "10:00 AM"),
            Events("Art Exhibition", "Gallery 3", "June 5, 2025", "02:00 PM")
        )

        // Sample data for Interested Events
        val interestedEvents = listOf(
            Events("Music Fest", "Outdoor Stage", "May 10, 2025", "07:00 PM"),
            Events("Startup Workshop", "Room 201", "May 15, 2025", "09:00 AM")
        )

        // Set up the adapters and layout managers for RecyclerViews
        eventsAdapterUpcoming = EventsAdapter(upcomingEvents)
        eventsAdapterInterested = EventsAdapter(interestedEvents)

        // Horizontal scrolling for upcoming events
        recyclerViewUpcoming.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Horizontal scrolling for interested events
        recyclerViewInterested.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        recyclerViewUpcoming.adapter = eventsAdapterUpcoming
        recyclerViewInterested.adapter = eventsAdapterInterested

        // Return the root view now that all the setup is complete
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
