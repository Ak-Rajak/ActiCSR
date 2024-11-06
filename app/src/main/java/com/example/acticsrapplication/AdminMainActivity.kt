package com.example.acticsrapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class AdminMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var adminbottomNavigationView: BottomNavigationView
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

        // Bottom navigation
        adminbottomNavigationView = findViewById(R.id.admin_bottom_navigation)
        adminbottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.admin_nav_home -> {
                    loadFragment(HomeFragment())
                }
                R.id.admin_nav_add_event -> {
                    loadFragment(AddEventFragment())
                }
                R.id.admin_nav_view_event -> {
                    loadFragment(EventsFragment())
                }
                R.id.admin_nav_view_reg -> {
                    loadFragment(ViewRegistrationsFragment())
                }
            }
            true // Return true to indicate item selection was handled
        }

        adminbottomNavigationView.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.admin_nav_home -> {
                    loadFragment(HomeFragment())
                }
                R.id.admin_nav_add_event -> {
                    loadFragment(AddEventFragment())
                }
                R.id.admin_nav_view_event -> {
                    loadFragment(EventsFragment())
                }
                R.id.admin_nav_view_reg -> {
                    loadFragment(ViewRegistrationsFragment())
                }
            }
        }


        // Set initial fragment when the activity is first created
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AdminHomeFragment()).commit()
            navigationView.setCheckedItem(R.id.admin_home)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.admin_home -> loadFragment(AdminHomeFragment())
            R.id.admin_add_event -> loadFragment(AddEventFragment())
            R.id.admin_view_event -> loadFragment(EventsFragment())
            R.id.admin_event_register -> loadFragment(ViewRegistrationsFragment())
            R.id.admin_about_us -> loadFragment(AboutFragment())
            R.id.admin_logout -> showLogoutConfirmationDialog()
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
