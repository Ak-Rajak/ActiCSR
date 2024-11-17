package com.example.acticsrapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class AdminHomeFragment : Fragment() {

    private lateinit var adminTitle: TextView
    private lateinit var activitiesOverviewTitle: TextView
    private lateinit var activitiesCount: TextView
    private lateinit var pendingActivities: TextView
    private lateinit var addActivityButton: Button
    private lateinit var viewActivitiesButton: Button
    private lateinit var recentActivitiesList: TextView
    private lateinit var notificationsList: TextView
    private lateinit var totalUsersStat: TextView
    private lateinit var completedActivitiesStat: TextView
    private lateinit var linkUserManagement: TextView
    private lateinit var linkSystemSettings: TextView
    private lateinit var upcomingEventsList: TextView
    private lateinit var systemStatusText: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_home, container, false)

        // Initialize views
        adminTitle = view.findViewById(R.id.admin_title)
        activitiesOverviewTitle = view.findViewById(R.id.activities_overview_title)
        activitiesCount = view.findViewById(R.id.activities_count)
        pendingActivities = view.findViewById(R.id.pending_activities)
        addActivityButton = view.findViewById(R.id.add_event_button)
        viewActivitiesButton = view.findViewById(R.id.view_events_button)
        recentActivitiesList = view.findViewById(R.id.recent_activities_list)
        notificationsList = view.findViewById(R.id.notifications_list)
        totalUsersStat = view.findViewById(R.id.total_users_stat)
        completedActivitiesStat = view.findViewById(R.id.completed_activities_stat)
        linkUserManagement = view.findViewById(R.id.link_user_management)
        linkSystemSettings = view.findViewById(R.id.link_system_settings)
        upcomingEventsList = view.findViewById(R.id.upcoming_events_list)
        systemStatusText = view.findViewById(R.id.system_status_text)

        // Setup actions
        setupListeners()

        return view
    }

    private fun setupListeners() {
        addActivityButton.setOnClickListener {
            // Create a new instance of AddEventFragment
            val fragment = AddEventFragment()

            // Use FragmentTransaction to replace the current fragment
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)  // Optional: add to back stack
            transaction.commit()
        }

        viewActivitiesButton.setOnClickListener {
            // Create a new instance of AddEventFragment
            val fragment = EventsAdminFragment()

            // Use FragmentTransaction to replace the current fragment
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)  // Optional: add to back stack
            transaction.commit()
        }

        linkUserManagement.setOnClickListener {
            // Navigate to User Management screen
        }

        linkSystemSettings.setOnClickListener {
            // Navigate to System Settings screen
        }
    }

    private fun updateDashboardData() {
        // Simulated data - replace with actual dynamic data
        activitiesCount.text = "Total Activities: 50"
        pendingActivities.text = "Pending Activities: 10"
        recentActivitiesList.text = "Activity 1, Activity 2, Activity 3"
        notificationsList.text = "You have 2 new notifications"
        totalUsersStat.text = "Users: 150"
        completedActivitiesStat.text = "Completed Activities: 40"
        upcomingEventsList.text = "Event A on 20th Nov, Event B on 25th Nov"
        systemStatusText.text = "All systems operational."
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateDashboardData() // Populate initial data
    }
}
