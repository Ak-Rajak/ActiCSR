package com.example.acticsrapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateEventFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var eventId: String
    private lateinit var titleEditText: EditText
    private lateinit var coordinatorNameEditText: EditText
    private lateinit var placeEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var dateTextView: TextView
    private lateinit var timeTextView: TextView

    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_event, container, false)

        // Initialize views
        titleEditText = view.findViewById(R.id.editTextEventTitle)
        coordinatorNameEditText = view.findViewById(R.id.editTextCoordinatorName)
        placeEditText = view.findViewById(R.id.editTextEventPlace)
        descriptionEditText = view.findViewById(R.id.editTextDescription)
        dateTextView = view.findViewById(R.id.textViewDate)
        timeTextView = view.findViewById(R.id.textViewTime)

        // Get eventId from arguments
        eventId = arguments?.getString("eventId") ?: ""

        // Load event data
        loadEventData()

        // Set Date and Time Pickers
        dateTextView.setOnClickListener { showDatePickerDialog() }
        timeTextView.setOnClickListener { showTimePickerDialog() }

        // Save button click listener
        view.findViewById<Button>(R.id.buttonSaveEvent).setOnClickListener {
            saveUpdatedEvent()
        }

        return view
    }

    private fun loadEventData() {
        db.collection("events").document(eventId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    titleEditText.setText(document.getString("title"))
                    coordinatorNameEditText.setText(document.getString("coordinatorName"))
                    placeEditText.setText(document.getString("place"))
                    descriptionEditText.setText(document.getString("description"))

                    // Safely retrieve and format date and time from Firestore
                    val dateTimestamp = document.getTimestamp("date")

                    if (dateTimestamp != null) {
                        val date = dateTimestamp.toDate()
                        dateTextView.text = dateFormat.format(date)  // Format the date
                        timeTextView.text = timeFormat.format(date)  // Format the time separately if needed
                    } else {
                        dateTextView.text = "Date not available"
                        timeTextView.text = "Time not available"
                    }
                } else {
                    Toast.makeText(context, "No event data found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to load event data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                dateTextView.text = dateFormat.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                timeTextView.text = timeFormat.format(calendar.time)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun saveUpdatedEvent() {
        val title = titleEditText.text.toString().trim()
        val coordinatorName = coordinatorNameEditText.text.toString().trim()
        val place = placeEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()
        val date = dateTextView.text.toString()
        val time = timeTextView.text.toString()

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(coordinatorName) || TextUtils.isEmpty(place) ||
            TextUtils.isEmpty(description) || TextUtils.isEmpty(date) || TextUtils.isEmpty(time)) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Parse date and time from the TextViews
        val updatedEventDate: Timestamp? = try {
            val parsedDate = dateFormat.parse(date)
            val parsedTime = timeFormat.parse(time)
            // Combining date and time
            val calendar = Calendar.getInstance()
            if (parsedDate != null && parsedTime != null) {
                calendar.time = parsedDate
                calendar.set(Calendar.HOUR_OF_DAY, parsedTime.hours)
                calendar.set(Calendar.MINUTE, parsedTime.minutes)
            }
            Timestamp(calendar.time)
        } catch (e: Exception) {
            null
        }

        val updatedEvent = hashMapOf(
            "title" to title,
            "coordinatorName" to coordinatorName,
            "place" to place,
            "description" to description,
            "date" to updatedEventDate
        )

        db.collection("events").document(eventId)
            .set(updatedEvent)
            .addOnSuccessListener {
                Toast.makeText(context, "Event updated successfully", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error updating event: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
