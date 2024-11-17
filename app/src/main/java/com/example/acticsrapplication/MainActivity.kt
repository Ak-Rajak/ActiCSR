package com.example.acticsrapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.eventmanager.AboutFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var bottomNavigationView: BottomNavigationView
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Set Firebase Database log level to DEBUG
        FirebaseDatabase.getInstance().setLogLevel(com.google.firebase.database.Logger.Level.DEBUG)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        // Set up the ActionBar toggle for the drawer
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.open_nav, R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_home -> loadFragment(HomeFragment())
                R.id.bottom_nav_forYou -> loadFragment(EventsFragment())
                R.id.bottom_nav_interest -> loadFragment(EventsFragment())
                R.id.bottom_nav_profile -> loadFragment(ProfileFragment())
            }
            true // Returning true to indicate item selection was handled
        }

        bottomNavigationView.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_home -> loadFragment(HomeFragment())
                R.id.bottom_nav_forYou -> loadFragment(EventsFragment())
                R.id.bottom_nav_interest -> loadFragment(EventsFragment())
                R.id.bottom_nav_profile -> loadFragment(ProfileFragment())
            }
        }


        // Load the initial fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
            navigationView.setCheckedItem(R.id.nav_home)
        }

        // Load user information into the navigation header
        loadUserData(navigationView)

        // Set click listeners for the user name and email
        val headerView = navigationView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.user_Name)
        val userEmailTextView = headerView.findViewById<TextView>(R.id.user_Email)

        userNameTextView.setOnClickListener { navigateToProfileFragment() }
        userEmailTextView.setOnClickListener { navigateToProfileFragment() }
    }
    private fun loadUserData(navigationView: NavigationView) {
        val user = firebaseAuth.currentUser
        user?.let {
            val headerView = navigationView.getHeaderView(0)
            val userNameTextView = headerView.findViewById<TextView>(R.id.user_Name)
            val userEmailTextView = headerView.findViewById<TextView>(R.id.user_Email)

            val userId = it.uid  // Get the authenticated user's UID

            // Retrieve user data from Firestore
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        // Extract and set the username and email from Firestore
                        val username = document.getString("username") ?: "User Name"
                        val email = document.getString("email") ?: "user_email@example.com"

                        // Update TextViews in NavigationView header
                        userNameTextView.text = username
                        userEmailTextView.text = email
                    } else {
                        Log.d("Firestore", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("Firestore", "Error fetching user data", exception)
                }
        }
    }

    private fun navigateToProfileFragment() {
        // Replace the current fragment with ProfileFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ProfileFragment())
            .commit()
        drawerLayout.closeDrawer(GravityCompat.START) // Close the drawer if it's open
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> loadFragment(HomeFragment())
            R.id.nav_settings -> loadFragment(SettingsFragment())
            R.id.nav_share -> loadFragment(EventsFragment())
            R.id.nav_about -> loadFragment(AboutFragment())
            R.id.nav_logout -> showLogoutConfirmationDialog()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun showLogoutConfirmationDialog() {
        // Inflate the custom layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.logout_alert_dialer, null)

        // Find the buttons and set up their actions
        val btnYes: Button = dialogView.findViewById(R.id.btn_yes)
        val btnNo: Button = dialogView.findViewById(R.id.btn_no)

        // Create the AlertDialog with the custom view
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Set button actions
        btnYes.setOnClickListener {
            // Handle logout action
            logout()
            dialog.dismiss()  // Close the dialog
        }

        btnNo.setOnClickListener {
            // Dismiss the dialog
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }


    private fun logout() {
        // Sign out from Firebase
        firebaseAuth.signOut()

        // Clear session data
        getSharedPreferences("userSession", MODE_PRIVATE).edit().clear().apply()

        // Redirect to SignIn activity
        Intent(this, SignIn::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
        }
        finish() // Close the current activity
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
