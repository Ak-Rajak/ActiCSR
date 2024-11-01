package com.example.acticsrapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class AboutEventFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about_event, container, false)

        // Initialize views
        val eventTitle = view.findViewById<TextView>(R.id.textViewEventTitle)
        val eventDescription = view.findViewById<TextView>(R.id.textViewEventDescription)
        val eventDate = view.findViewById<TextView>(R.id.textViewEventDate)
        val eventLocation = view.findViewById<TextView>(R.id.textViewEventLocation)
        val buttonMoreInfo = view.findViewById<Button>(R.id.buttonMoreInfo)

        // Set content (for demonstration, you can set actual data dynamically)
        eventTitle.text = "Annual Tech Conference"
        eventDescription.text = "Join us for the annual tech conference where experts from the industry discuss the latest trends in technology."
        eventDate.text = "Date: June 15, 2024"
        eventLocation.text = "Location: Main Auditorium"

        // Set a click listener for the More Info button
        buttonMoreInfo.setOnClickListener {
            Toast.makeText(activity, "More event information coming soon!", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
