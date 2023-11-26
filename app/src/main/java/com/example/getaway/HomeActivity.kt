package com.example.getaway

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private val homeFragment: Fragment = HomeFragment()
    private val groupsFragment: Fragment = GroupsFragment()
    private val profileFragment: Fragment = ProfileFragment()
    private val fragmentManager = supportFragmentManager
    private var activeFragment: Fragment = homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Initialize fragments
        fragmentManager.beginTransaction().apply {
            add(R.id.fragmentContainer, profileFragment, "3")
            hide(profileFragment)
            add(R.id.fragmentContainer, groupsFragment, "2")
            hide(groupsFragment)
            add(R.id.fragmentContainer, homeFragment, "1")
        }.commit()

        // Set up bottom navigation view
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    switchFragment(homeFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_groups -> {
                    switchFragment(groupsFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    switchFragment(profileFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
    }

    private fun switchFragment(fragment: Fragment) {
        fragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit()
        activeFragment = fragment
    }
}
