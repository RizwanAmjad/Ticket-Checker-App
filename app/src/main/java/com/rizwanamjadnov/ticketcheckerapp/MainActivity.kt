package com.rizwanamjadnov.ticketcheckerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rizwanamjadnov.ticketcheckerapp.fragments.TicketCreateFragment
import com.rizwanamjadnov.ticketcheckerapp.fragments.TicketListFragment
import com.rizwanamjadnov.ticketcheckerapp.fragments.TicketScanFragment

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fragmentContainerView: FragmentContainerView

    private lateinit var ticketListFragment: TicketListFragment
    private lateinit var ticketCreateFragment: TicketCreateFragment
    private lateinit var ticketScanFragment: TicketScanFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ticketListFragment = TicketListFragment()
        ticketCreateFragment = TicketCreateFragment()
        ticketScanFragment = TicketScanFragment()

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        fragmentContainerView = findViewById(R.id.navFragmentContainerView)

        navigateToFragment(ticketListFragment)

        // handle bottom navigation
        bottomNavigationView.setOnItemSelectedListener {
            return@setOnItemSelectedListener when(it.itemId){
                R.id.navTicketList->{
                    navigateToFragment(ticketListFragment)
                    true
                }
                R.id.navTicketCreate->{
                    navigateToFragment(ticketCreateFragment)
                    true
                }
                R.id.navTicketScan->{
                    navigateToFragment(ticketScanFragment)
                    true
                }

                else->false
            }
        }
    }
    private fun navigateToFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.navFragmentContainerView, fragment)
            commit()
        }
    }
}