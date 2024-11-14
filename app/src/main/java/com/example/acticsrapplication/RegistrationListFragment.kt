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
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : RegistratedUserAdapter
    private val registeredUsers = mutableListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registration_list, container,false)

        recyclerView = view.findViewById(R.id.registrationRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = RegistratedUserAdapter(registeredUsers)
        recyclerView.adapter = adapter

        fetchRegisteredUsers()

        return view

    }

    private fun  fetchRegisteredUsers() {
        val db = FirebaseFirestore.getInstance()

        db.collection("registrations")
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                registeredUsers.clear()

                for (doc in queryDocumentSnapshots) {
                    val userId = doc.getString("userId")

                    if(userId != null) {
                        db.collection("users").document(userId).get()
                            .addOnSuccessListener { userDoc ->
                                if(userDoc.exists()) {
                                    val email = userDoc.getString("email")
                                    if (email != null) {
                                        registeredUsers.add(User(userId, email))
                                        adapter.notifyDataSetChanged()
                                    }
                                }
                            }
                    }
                }
            }

            .addOnFailureListener { e ->
                Log.w("RegistrationList" , "Error fetching registration" , e)
            }
    }

}