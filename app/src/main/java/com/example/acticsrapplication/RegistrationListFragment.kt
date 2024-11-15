package com.example.acticsrapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationListFragment : Fragment() {

    private var eventId: String? = null
    private val db = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RegistrationAdapter
    private val registrations = mutableListOf<Registration>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_registration_list, container, false)

        // Retrieve eventId from arguments
        eventId = arguments?.getString("eventId")
        Log.d("RegistrationList", "Event ID passed to fragment: $eventId")

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewRegistrations)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = RegistrationAdapter(registrations)
        recyclerView.adapter = adapter

        // Load registrations for the event if eventId is valid
        eventId?.let {
            loadRegistrations(it)
        } ?: Log.d("RegistrationList", "Event ID is null")

        return view
    }

    private fun loadRegistrations(eventId: String) {
        db.collection("event_registrations")
            .document(eventId)  // Query by the document ID (eventId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Log and retrieve the "registrations" field
                    val registrationsList = document.get("registrations") as? List<Map<String, Any>>
                    if (registrationsList != null) {
                        registrations.clear()
                        for (registration in registrationsList) {
                            val username = registration["username"] as? String ?: "Unknown"
                            val email = registration["email"] as? String ?: "Unknown"
                            Log.d("RegistrationList", "Loaded registration: username=$username, email=$email")
                            registrations.add(Registration(username, email))
                        }
                        adapter.notifyDataSetChanged()
                    } else {
                        Log.d("RegistrationList", "No registrations found for eventId: $eventId")
                    }
                } else {
                    Log.d("RegistrationList", "Event document not found for eventId: $eventId")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("RegistrationList", "Error loading registrations", exception)
            }
    }
}
