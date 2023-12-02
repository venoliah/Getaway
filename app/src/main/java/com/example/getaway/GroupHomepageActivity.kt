package com.example.getaway

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class GroupHomepageActivity : AppCompatActivity() {

    private val goalsFragment = GoalsFragment()
    private val activityFragment = ActivityFragment()
    private val membersFragment = MembersFragment()
    private val contributionFragment = ContributionFragment() // Added ContributionFragment

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_goals -> {
                    replaceFragment(goalsFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_activity -> {
                    replaceFragment(activityFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_members -> {
                    replaceFragment(membersFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_contribution -> { // Added ContributionFragment
                    replaceFragment(contributionFragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grouphomepage)

        // Retrieve the group name from the Intent
        val groupName = intent.getStringExtra("groupName")

        // Find the TextView by ID
        val groupNameTextView = findViewById<TextView>(R.id.groupNameTextView)

        // Set the group name in the TextView
        groupNameTextView.text = groupName

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        // Default fragment when the activity starts
        replaceFragment(goalsFragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
