package com.example.acticsrapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class AdminHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_home, container, false)

        // Initialize views
        val textViewWelcome = view.findViewById<TextView>(R.id.textViewWelcome)
        val buttonAction = view.findViewById<Button>(R.id.buttonAction)

        // Set a click listener for the button
        buttonAction.setOnClickListener {
            Toast.makeText(activity, "Action performed!", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
