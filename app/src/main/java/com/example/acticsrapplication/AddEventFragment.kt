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
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class AddEventFragment : Fragment() {

    private val firestore = FirebaseFirestore.getInstance()
    private var selectedDate: Calendar? = null // To store selected date for validation
    private var selectedTime: Calendar? = null // To store selected time for validation

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
                editTextDate.setText("$day/${month + 1}/$year")
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Time picker with 12-hour format
        editTextTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                selectedTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                }
                val amPm = if (selectedHour < 12) "AM" else "PM"
                val hourIn12Format = if (selectedHour % 12 == 0) 12 else selectedHour % 12
                editTextTime.setText(String.format("%02d:%02d %s", hourIn12Format, selectedMinute, amPm))
            }, hour, minute, false).show() // 'false' sets 12-hour format
        }

        // Save Event button click listener
        buttonSaveEvent.setOnClickListener {
            val title = editTextTitle.text.toString().trim()
            val coordinatorName = editTextCoordinatorName.text.toString().trim()
            val place = editTextPlace.text.toString().trim()
            val description = editTextDescription.text.toString().trim()

            // Validate fields
            if (title.isEmpty() || coordinatorName.isEmpty() || editTextDate.text.isEmpty() || editTextTime.text.isEmpty() || place.isEmpty()) {
                Toast.makeText(activity, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate that the selected date is not in the past
            selectedDate?.let { date ->
                val currentDate = Calendar.getInstance()
                if (date.before(currentDate)) {
                    Toast.makeText(activity, "You can't add event on past date", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // Combine date and time
            val eventDateTime = Calendar.getInstance().apply {
                selectedDate?.let { date ->
                    set(Calendar.YEAR, date.get(Calendar.YEAR))
                    set(Calendar.MONTH, date.get(Calendar.MONTH))
                    set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH))
                }
                selectedTime?.let { time ->
                    set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY))
                    set(Calendar.MINUTE, time.get(Calendar.MINUTE))
                }
            }.time

            // Save event to Firestore with Timestamp
            saveEventToFirestore(title, coordinatorName, Timestamp(eventDateTime), place, description)
        }

        return view
    }

    private fun saveEventToFirestore(
        title: String,
        coordinatorName: String,
        date: Timestamp,
        place: String,
        description: String
    ) {
        val event = hashMapOf(
            "title" to title,
            "coordinatorName" to coordinatorName,
            "date" to date,  // Use Timestamp here
            "place" to place,
            "description" to description
        )

        firestore.collection("events").add(event)
            .addOnSuccessListener { documentReference ->
                val eventId = documentReference.id
                documentReference.update("eventId", eventId)
                    .addOnSuccessListener {
                        Toast.makeText(activity, "Event saved successfully!", Toast.LENGTH_SHORT).show()
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
