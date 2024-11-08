package com.example.acticsrapplication

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class EventDetailsFragment : Fragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Retrieve eventId from the arguments
        eventId = arguments?.getString("eventId")

        // Inflate the layout for this fragment
        val binding = inflater.inflate(R.layout.fragment_event_main, container, false)

        // Bind UI elements manually
        eventTitle = binding.findViewById(R.id.eventTitle)
        eventPlace = binding.findViewById(R.id.eventPlace)
        eventTime = binding.findViewById(R.id.eventTime)
        eventDate = binding.findViewById(R.id.eventDate)
        eventDescription = binding.findViewById(R.id.eventDescription)
        eventCoordinatorName = binding.findViewById(R.id.eventCoordinatorName)
        registerButton = binding.findViewById(R.id.registerButton)

        // Load event data based on eventId
        loadEventDetails()

        // Set up onclick
        registerButton.setOnClickListener {
            showRegistrationConfirmationDialog()
        }
        return binding
    }

    private fun loadEventDetails() {
        // Check if the eventId is null or empty
        eventId?.let { id ->
            db.collection("events")
                .document(id)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Retrieve data from the Firestore document
                        val title = document.getString("title") ?: "Unknown Title"
                        val place = document.getString("place") ?: "Unknown Location"
                        val time = document.getString("time") ?: "Unknown Time"
                        val date = document.getString("date") ?: "Unknown Date"
                        val description = document.getString("description") ?: "No Description"
                        val coordinatorName = document.getString("coordinatorName") ?: "Unknown"

                        // Log the data for debugging
                        Log.d("EventDetailsFragment", "Event Data: Title = $title, Place = $place, Time = $time")

                        // Update the UI with the retrieved event details
                        eventTitle.text = title
                        eventPlace.text = place
                        eventCoordinatorName.text = coordinatorName
                        eventDescription.text = description

                        // Set separate date and time TextViews
                        eventDate.text = date
                        eventTime.text = time
                    } else {
                        // Handle the case when the document does not exist
                        Log.d("EventDetailsFragment", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors during Firestore retrieval
                    Log.e("EventDetailsFragment", "Error fetching event details: ", exception)
                }
        } ?: run {
            // Handle the case when eventId is null
            Log.e("EventDetailsFragment", "Event ID is null or empty")
        }
    }

    private fun showRegistrationConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm registration")
            .setMessage("Do you want to register for this events ?")
            .setIcon(R.drawable.baseline_interests_24)
            .setPositiveButton("Yes") {_,_ ->
//                registerForEvent()
            }
            .setNegativeButton("No", null)
            .show()
    }


}
