package com.example.acticsrapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class AdminMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        // Initialize FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        drawerLayout = findViewById(R.id.drawer_layout)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set initial fragment when the activity is first created
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AdminHomeFragment()).commit()
            navigationView.setCheckedItem(R.id.admin_home)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.admin_home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, AdminHomeFragment()).commit()
            }
            R.id.admin_add_event -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, AddEventFragment()).commit()
            }
            R.id.admin_view_event -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, EventsFragment()).commit()
            }
            R.id.admin_event_register -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ViewRegistrationsFragment()).commit()
            }
            R.id.admin_about_us -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, AboutFragment()).commit()
            }
            R.id.admin_logout -> {
                showLogoutConfirmationDialog()
                Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
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
