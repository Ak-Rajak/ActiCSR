package com.example.acticsrapplication

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

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
    private lateinit var progressBar: ProgressBar

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
        progressBar = binding.findViewById(R.id.progressBar)

        // Load event data based on eventId
        loadEventDetails()

        // Set up onclick listener for the register button
        registerButton.setOnClickListener {
            showRegistrationConfirmationDialog()
        }
        return binding
    }

    private fun loadEventDetails() {
        eventId?.let { id ->
            progressBar.visibility = View.VISIBLE  // Show loading progress bar
            db.collection("events")
                .document(id)
                .get()
                .addOnSuccessListener { document ->
                    progressBar.visibility = View.GONE  // Hide loading progress bar
                    if (document.exists()) {
                        val title = document.getString("title") ?: "Unknown Title"
                        val place = document.getString("place") ?: "Unknown Location"
                        val coordinatorName = document.getString("coordinatorName") ?: "Unknown Coordinator"
                        val description = document.getString("description") ?: "No Description"

                        val dateTimestamp = document.getTimestamp("date")
                        val dateTimeString = if (dateTimestamp != null) {
                            // Format the timestamp into a readable date and time format
                            val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(dateTimestamp.toDate())
                            val formattedTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(dateTimestamp.toDate())

                            // Split the date and time into separate values for different TextViews
                            eventDate.text = formattedDate  // Display the date
                            eventTime.text = formattedTime  // Display the time
                        } else {
                            eventDate.text = "Unknown Date"
                            eventTime.text = "Unknown Time"
                        }

                        // Log the data for debugging
                        Log.d("EventDetailsFragment", "Event Data: Title = $title, Place = $place, Date = ${eventDate.text}, Time = ${eventTime.text}")

                        // Update the UI with the retrieved event details
                        eventTitle.text = title
                        eventPlace.text = place
                        eventCoordinatorName.text = coordinatorName
                        eventDescription.text = description
                    } else {
                        Log.d("EventDetailsFragment", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    progressBar.visibility = View.GONE  // Hide loading progress bar
                    Log.e("EventDetailsFragment", "Error fetching event details: ", exception)
                }
        } ?: run {
            Log.e("EventDetailsFragment", "Event ID is null or empty")
        }
    }

    private fun showRegistrationConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm registration")
            .setMessage("Do you want to register for this event?")
            .setIcon(R.drawable.baseline_interests_24)
            .setPositiveButton("Yes") { _, _ ->
                registerForEvent()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun registerForEvent() {
        eventId?.let { id ->
            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
            val registrationData = mutableMapOf<String, Any>(
                "eventId" to id, // Store eventId directly in the registration data
                "userId" to (currentUserId ?: ""), // Store userId
                "timestamp" to Timestamp.now() // Store the current timestamp
            )

            // Parse date and time to Firestore Date
            try {
                val dateStr = eventDate.text.toString().trim()
                val timeStr = eventTime.text.toString().trim()
                val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
                val dateTimeStr = "$dateStr $timeStr"
                val eventDateTime = dateFormat.parse(dateTimeStr)

                if (eventDateTime != null) {
                    registrationData["eventDate"] = Timestamp(eventDateTime)
                }
            } catch (e: Exception) {
                Log.e("EventDetailsFragment", "Error parsing date and time", e)
            }

            // Save registration data in the "registrations" collection
            if (currentUserId != null) {
                db.collection("registrations")
                    .add(registrationData) // Add a new document in the "registrations" collection
                    .addOnSuccessListener {
                        Log.d("EventDetailsFragment", "Registration successful!")
                        showSuccessMessage("Registered successfully!")
                    }
                    .addOnFailureListener { e ->
                        Log.e("EventDetailsFragment", "Error registering for event", e)
                    }
            }
        } ?: Log.e("EventDetailsFragment", "Event ID is null or empty")
    }

    private fun showSuccessMessage(message: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
