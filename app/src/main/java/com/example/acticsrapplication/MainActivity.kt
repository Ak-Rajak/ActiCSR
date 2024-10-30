package com.example.acticsrapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Set Firebase Database log level to DEBUG
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG) // Correct log level setup

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
        // Create an AlertDialog to confirm logout
        AlertDialog.Builder(this).apply {
            setTitle("Logout")
            setMessage("Are you sure you want to logout?")
            setPositiveButton("Yes") { _, _ -> logout() }
            setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            create().show()
        }
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
