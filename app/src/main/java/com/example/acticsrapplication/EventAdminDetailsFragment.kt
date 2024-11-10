package com.example.acticsrapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class EventAdminDetailsFragment : Fragment() {

    private var eventId: String? = null
    private val db = FirebaseFirestore.getInstance() // Initialize Firestore

    // UI Elements for displaying event data
    private lateinit var eventTitle: TextView
    private lateinit var eventPlace: TextView
    private lateinit var eventTime: TextView
    private lateinit var eventDate: TextView
    private lateinit var eventDescription: TextView
    private lateinit var eventCoordinatorName: TextView
    private lateinit var registerButton: Button
    private lateinit var progressBar: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Retrieve eventId from the arguments
        eventId = arguments?.getString("eventId")

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_event_admin_main, container, false)

        // Bind UI elements
        eventTitle = view.findViewById(R.id.eventTitle1)
        eventPlace = view.findViewById(R.id.eventPlace1)
        eventTime = view.findViewById(R.id.eventTime1)
        eventDate = view.findViewById(R.id.eventDate1)
        eventDescription = view.findViewById(R.id.eventDescription1)
        eventCoordinatorName = view.findViewById(R.id.eventCoordinatorName1)
        registerButton = view.findViewById(R.id.registerButton1)
        progressBar = view.findViewById(R.id.progressBar1)

        // Load event data based on eventId
        eventId?.let { loadEventDetails(it) }

        // Set up onClick listener for the register button if needed
        registerButton.setOnClickListener {
            // Code to handle registration (if implemented)
        }
        return view
    }

    private fun loadEventDetails(eventId: String) {
        // Show loading progress bar
        progressBar.visibility = View.VISIBLE

        db.collection("events")
            .document(eventId)
            .get()
            .addOnSuccessListener { document ->
                // Hide progress bar when data is fetched
                progressBar.visibility = View.GONE

                if (document.exists()) {
                    // Retrieve event data from document
                    val title = document.getString("title") ?: "Unknown Title"
                    val place = document.getString("place") ?: "Unknown Location"
                    val coordinatorName = document.getString("coordinatorName") ?: "Unknown Coordinator"
                    val description = document.getString("description") ?: "No Description"

                    val dateTimestamp = document.getTimestamp("date")
                    val formattedDate = dateTimestamp?.toDate()?.let {
                        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(it)
                    } ?: "Unknown Date"

                    val formattedTime = dateTimestamp?.toDate()?.let {
                        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(it)
                    } ?: "Unknown Time"

                    // Update UI with the retrieved event details
                    eventTitle.text = title
                    eventPlace.text = place
                    eventCoordinatorName.text = coordinatorName
                    eventDescription.text = description
                    eventDate.text = formattedDate
                    eventTime.text = formattedTime

                    // Log the data for debugging
                    Log.d("EventAdminDetails", "Event Data: Title = $title, Place = $place, Date = $formattedDate, Time = $formattedTime")
                } else {
                    Log.d("EventAdminDetails", "No document found for event ID: $eventId")
                }
            }
            .addOnFailureListener { exception ->
                // Hide progress bar and log error
                progressBar.visibility = View.GONE
                Log.e("EventAdminDetails", "Error fetching event details: ", exception)
            }
    }
}
