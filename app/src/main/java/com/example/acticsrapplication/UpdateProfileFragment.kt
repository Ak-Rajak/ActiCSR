package com.example.acticsrapplication

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class UpdateProfileFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var editTextName: EditText
    private lateinit var editTextStudentId: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextMobile: EditText
    private lateinit var editTextDOB: EditText
    private lateinit var editTextCourse: EditText
    private lateinit var editTextYearOfStudy: EditText
    private lateinit var editTextMajorMinor: EditText
    private lateinit var editTextAcademicYear: EditText
    private lateinit var editTextAlternateEmail: EditText
    private lateinit var editTextSocialMediaLinks: EditText
    private lateinit var buttonSave: Button

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_profile, container, false)

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Reference to UI elements
        editTextName = view.findViewById(R.id.editTextName)
        editTextStudentId = view.findViewById(R.id.editTextStudentId)
        editTextEmail = view.findViewById(R.id.editTextEmail)
        editTextMobile = view.findViewById(R.id.editTextMobile)
        editTextDOB = view.findViewById(R.id.editTextDOB)
        editTextCourse = view.findViewById(R.id.editTextCourse)
        editTextYearOfStudy = view.findViewById(R.id.editTextYearOfStudy)
        editTextMajorMinor = view.findViewById(R.id.editTextMajorMinor)
        editTextAcademicYear = view.findViewById(R.id.editTextAcademicYear)
        editTextAlternateEmail = view.findViewById(R.id.editTextAlternateEmail)
        editTextSocialMediaLinks = view.findViewById(R.id.editTextSocialMediaLinks)
        buttonSave = view.findViewById(R.id.buttonSave)

        // Load current user data
        loadUserData()

        // Set date picker dialog for DOB field
        editTextDOB.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    editTextDOB.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Set click listener for save button
        buttonSave.setOnClickListener {
            saveUserData()
        }

        return view
    }

    private fun loadUserData() {
        val user = firebaseAuth.currentUser
        user?.let {
            val userId = it.uid
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        editTextName.setText(document.getString("username"))
                        editTextStudentId.setText(document.getString("studentId"))
                        editTextEmail.setText(document.getString("email"))
                        editTextMobile.setText(document.getString("mobile"))

                        // Attempt to get DOB in multiple formats
                        val dob = document.get("dob")
                        when (dob) {
                            is Timestamp -> {
                                editTextDOB.setText(dob.toDate().let { dateFormat.format(it) })
                            }
                            is String -> {
                                editTextDOB.setText(dob)
                            }
                            // You can add more cases here if you expect other formats
                            else -> {
                                editTextDOB.setText("") // Handle cases where dob is null or not recognized
                            }
                        }

                        editTextCourse.setText(document.getString("course"))
                        editTextYearOfStudy.setText(document.getString("yearOfStudy"))
                        editTextMajorMinor.setText(document.getString("majorMinor"))
                        editTextAcademicYear.setText(document.getString("academicYear"))
                        editTextAlternateEmail.setText(document.getString("alternateEmail"))
                        editTextSocialMediaLinks.setText(document.getString("socialMediaLinks"))
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error fetching user data", e)
                }
        }
    }

    private fun saveUserData() {
        val user = firebaseAuth.currentUser
        user?.let {
            val userId = it.uid

            // Define the date format for parsing
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            // Parse and convert DOB to Timestamp if provided
            val dobText = editTextDOB.text.toString()
            val dobTimestamp = dobText.takeIf { it.isNotEmpty() }?.let { dateString ->
                try {
                    // Attempt to parse the date and convert it to a Timestamp
                    val date: Date? = dateFormat.parse(dateString)
                    date?.let { Timestamp(it) }
                } catch (e: Exception) {
                    Log.e("DateParse", "Date parsing failed for $dateString", e)
                    null // Return null if parsing fails
                }
            }

            // Prepare user data map
            val userData = hashMapOf(
                "username" to editTextName.text.toString(),
                "studentId" to editTextStudentId.text.toString(),
                "email" to editTextEmail.text.toString(),
                "mobile" to editTextMobile.text.toString(),

                // Store DOB as Timestamp if not null
                "dob" to dobTimestamp,

                "course" to editTextCourse.text.toString(),
                "yearOfStudy" to editTextYearOfStudy.text.toString(),
                "majorMinor" to editTextMajorMinor.text.toString(),
                "academicYear" to editTextAcademicYear.text.toString(),
                "alternateEmail" to editTextAlternateEmail.text.toString(),
                "socialMediaLinks" to editTextSocialMediaLinks.text.toString()
            )

            // Update data in Firestore
            db.collection("users").document(userId).set(userData)
                .addOnSuccessListener {
                    // Data saved successfully, navigate back to ProfileFragment
                    requireActivity().supportFragmentManager.popBackStack()
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error saving user data", e)
                }
        }
    }}
