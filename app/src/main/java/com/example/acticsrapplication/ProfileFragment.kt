package com.example.acticsrapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var profilePhoto: ImageView
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var userMobile: TextView
    private lateinit var userAcademicYear: TextView
    private lateinit var userStudentId: TextView
    private lateinit var userDOB: TextView
    private lateinit var userCourse: TextView
    private lateinit var userYearOfStudy: TextView
    private lateinit var userMajorMinor: TextView
    private lateinit var userAlternateEmail: TextView
    private lateinit var userSocialMediaLinks: TextView
    private lateinit var updateButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize Firebase Auth and Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Get references to UI elements
        profilePhoto = view.findViewById(R.id.profile_photo)
        userName = view.findViewById(R.id.full_name)
        userEmail = view.findViewById(R.id.user_Email)
        userMobile = view.findViewById(R.id.user_Mobile)
        userAcademicYear = view.findViewById(R.id.user_AcademicYear)
        userStudentId = view.findViewById(R.id.student_id)
        userDOB = view.findViewById(R.id.user_DOB)
        userCourse = view.findViewById(R.id.course_program)
        userYearOfStudy = view.findViewById(R.id.year_of_study)
        userMajorMinor = view.findViewById(R.id.major_minor)
        userAlternateEmail = view.findViewById(R.id.alternate_email)
        userSocialMediaLinks = view.findViewById(R.id.social_media_links)
        updateButton = view.findViewById(R.id.update_profile_button)

        // Load user data
        loadUserData()

        // Set click listener for the update button
        updateButton.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, UpdateProfileFragment()) // Use your container's ID
            transaction.addToBackStack(null) // Add to back stack to allow for back navigation
            transaction.commit()
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
                        displayUserData(document)
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error fetching user data", e)
                }
        }

        // Always show the update button
        updateButton.visibility = View.VISIBLE
    }

    private fun displayUserData(document: DocumentSnapshot) {
        // Retrieve and display user data from Firestore document
        userName.text = document.getString("username") ?: "No Name"
        userEmail.text = document.getString("email") ?: "No Email"
        userMobile.text = document.getString("mobile") ?: "No Mobile"
        userAcademicYear.text = document.getString("academicYear") ?: "No Academic Year"
        userStudentId.text = document.getString("studentId") ?: "No Student ID"

        // Format dateOfBirth if it exists as a Timestamp
        val dobTimestamp = document.getTimestamp("dateOfBirth")
        userDOB.text = dobTimestamp?.toDate()?.let { date ->
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
        } ?: "No Date of Birth"

        userCourse.text = document.getString("course") ?: "No Course"
        userYearOfStudy.text = document.getString("yearOfStudy") ?: "No Year of Study"
        userMajorMinor.text = document.getString("majorMinor") ?: "No Major/Minor"
        userAlternateEmail.text = document.getString("alternateEmail") ?: "No Alternate Email"
        userSocialMediaLinks.text = document.getString("socialMediaLinks") ?: "No Social Media Links"

        // Load profile photo if URL is stored
        val profilePicUrl = document.getString("profilePicUrl")
        Glide.with(this)
            .load(profilePicUrl)
            .placeholder(R.drawable.aklogo) // Placeholder image resource
            .error(R.drawable.error_image) // Error image resource
            .into(profilePhoto)
    }
}
