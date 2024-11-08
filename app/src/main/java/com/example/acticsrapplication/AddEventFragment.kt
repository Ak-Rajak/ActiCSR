package com.example.acticsrapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class AddEventFragment : Fragment() {

    private val firestore = FirebaseFirestore.getInstance()
    private var selectedDate: Calendar? = null // To store selected date for validation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_event, container, false)

        val editTextTitle = view.findViewById<EditText>(R.id.editTextTitle)
        val editTextCoordinatorName = view.findViewById<EditText>(R.id.editTextCoordinatorName)
        val editTextDate = view.findViewById<EditText>(R.id.editTextDate)
        val editTextTime = view.findViewById<EditText>(R.id.editTextTime)
        val editTextPlace = view.findViewById<EditText>(R.id.editTextPlace)
        val editTextDescription = view.findViewById<EditText>(R.id.editTextDescription)
        val buttonSaveEvent = view.findViewById<Button>(R.id.buttonSaveEvent)

        // Date picker
        editTextDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, year, month, day ->
                selectedDate = Calendar.getInstance().apply {
                    set(year, month, day)
                }

                // Update EditText with selected date
                editTextDate.setText("$day/${month + 1}/$year")
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Time picker with 12-hour format
        editTextTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                val amPm = if (selectedHour < 12) "AM" else "PM"
                val hourIn12Format = if (selectedHour % 12 == 0) 12 else selectedHour % 12
                editTextTime.setText(String.format("%02d:%02d %s", hourIn12Format, selectedMinute, amPm))
            }, hour, minute, false).show() // 'false' sets 12-hour format
        }

        // Save Event button click listener
        buttonSaveEvent.setOnClickListener {
            val title = editTextTitle.text.toString().trim()
            val coordinatorName = editTextCoordinatorName.text.toString().trim()
            val date = editTextDate.text.toString().trim()
            val time = editTextTime.text.toString().trim()
            val place = editTextPlace.text.toString().trim()
            val description = editTextDescription.text.toString().trim()

            // Validate fields
            if (title.isEmpty() || coordinatorName.isEmpty() || date.isEmpty() || time.isEmpty() || place.isEmpty()) {
                Toast.makeText(activity, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate that the selected date is not in the past
            selectedDate?.let {
                val currentDate = Calendar.getInstance()
                if (it.before(currentDate)) {
                    Toast.makeText(activity, "You can't add event on past date", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // Save event to Firestore
            saveEventToFirestore(title, coordinatorName, date, time, place, description)
        }

        return view
    }

    private fun saveEventToFirestore(
        title: String,
        coordinatorName: String,
        date: String,
        time: String,
        place: String,
        description: String
    ) {
        // Initial event data without eventId
        val event = hashMapOf(
            "title" to title,
            "coordinatorName" to coordinatorName,
            "date" to date,
            "time" to time,
            "place" to place,
            "description" to description
        )

        // Add event to Firestore
        firestore.collection("events").add(event)
            .addOnSuccessListener { documentReference ->
                // Retrieve auto-generated ID and add it as eventId
                val eventId = documentReference.id
                documentReference.update("eventId", eventId)
                    .addOnSuccessListener {
                        Toast.makeText(activity, "Event saved successfully!", Toast.LENGTH_SHORT).show()

                        // Redirect to EventsFragment
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.fragment_container, EventsFragment())
                            ?.addToBackStack(null)
                            ?.commit()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(activity, "Failed to save event ID: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(activity, "Failed to save event: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
