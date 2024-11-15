package com.example.eventmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.acticsrapplication.R

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_about, container, false)

        // Initialize the views from the XML layout
        val aboutUsTitle: TextView = rootView.findViewById(R.id.aboutUsTitle)
        val appDescription: TextView = rootView.findViewById(R.id.appDescription)
        val teamTitle: TextView = rootView.findViewById(R.id.teamTitle)
        val teamMembers: TextView = rootView.findViewById(R.id.teamMembers)
        val contactTitle: TextView = rootView.findViewById(R.id.contactTitle)
        val contactInfo: TextView = rootView.findViewById(R.id.contactInfo)
        val socialMediaTitle: TextView = rootView.findViewById(R.id.socialMediaTitle)
        val socialLinks: TextView = rootView.findViewById(R.id.socialLinks)

        // Set text content dynamically if needed (optional)
        aboutUsTitle.text = "About Us"
        appDescription.text = "Welcome to our Event Manager and Activity Tracker App! This app helps you organize events and track activities efficiently. Whether you're planning a conference, workshop, or any activity, this app simplifies event management and keeps you on track."
        teamTitle.text = "Our Development Team"
        teamMembers.text = "Sayan - Lead Developer\nJohn Doe - UI/UX Designer\nJane Smith - Backend Developer"
        contactTitle.text = "Contact Us"
        contactInfo.text = "Email: support@eventtrackerapp.com\nPhone: +123 456 7890"
        socialMediaTitle.text = "Follow Us"
        socialLinks.text = "Facebook: /eventtracker\nTwitter: @eventtracker\nInstagram: @event_tracker"

        return rootView
    }
}
