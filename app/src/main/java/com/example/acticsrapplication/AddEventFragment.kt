package com.example.acticsrapplication

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class AddEventFragment : Fragment() {

    // private lateinit var bannerUri: Uri  // Commenting out bannerUri
    private val firestore = FirebaseFirestore.getInstance()

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_event, container, false)

        val imageViewBanner = view.findViewById<ImageView>(R.id.imageViewBanner)
        val buttonSelectImage = view.findViewById<Button>(R.id.buttonSelectImage)
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
                editTextDate.setText("$day/${month + 1}/$year")
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Time picker
        editTextTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(requireContext(), { _, hour, minute ->
                editTextTime.setText(String.format("%02d:%02d", hour, minute))
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        // Select Image (Commenting out for now)
        buttonSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        // Save Event (skipping image upload)
        buttonSaveEvent.setOnClickListener {
            val title = editTextTitle.text.toString()
            val coordinatorName = editTextCoordinatorName.text.toString()
            val date = editTextDate.text.toString()
            val time = editTextTime.text.toString()
            val place = editTextPlace.text.toString()
            val description = editTextDescription.text.toString()

            // Temporarily comment out the check for bannerUri initialization
            // if (!this::bannerUri.isInitialized) {
            //     Toast.makeText(activity, "Please select a banner image", Toast.LENGTH_SHORT).show()
            //     return@setOnClickListener
            // }

            // Call saveEventToFirestore without passing a banner URL for now
            saveEventToFirestore(title, coordinatorName, date, time, place, description, null)
        }

        return view
    }

    private fun saveEventToFirestore(
        title: String,
        coordinatorName: String,
        date: String,
        time: String,
        place: String,
        description: String,
        bannerUrl: String?
    ) {
        val event = hashMapOf(
            "title" to title,
            "coordinatorName" to coordinatorName,
            "date" to date,
            "time" to time,
            "place" to place,
            "description" to description,
            "bannerUrl" to bannerUrl // This will be null for now
        )

        firestore.collection("events").add(event)
            .addOnSuccessListener {
                Toast.makeText(activity, "Event saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(activity, "Failed to save event: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Image selection (for future use)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            // Commenting out the bannerUri setting temporarily
            // bannerUri = data?.data ?: return
            view?.findViewById<ImageView>(R.id.imageViewBanner)?.setImageURI(data?.data)
        }
    }
}
