package com.example.mediscan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.mediscan.Fragments.HomeFragment
import com.example.mediscan.Fragments.PillsFragment
import com.example.mediscan.Fragments.ProfileFragment
import com.example.mediscan.Fragments.SavedFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Diffrent Screens
        val homeFragment = HomeFragment()
        val pillsdFragment = PillsFragment()
        val savedFragment = SavedFragment()
        val profileFragment = ProfileFragment()

        //Sets Home Screen
        setCurrentFragment(homeFragment)

        //Bottom Navigation
        bottom_nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home ->{
                    setCurrentFragment(homeFragment)
                    Log.i(TAG,"Home Selected")
                }
                R.id.pills ->{
                    setCurrentFragment(pillsdFragment)
                    Log.i(TAG,"Pills Selected")
                }
                R.id.saved ->{
                    setCurrentFragment(savedFragment)
                    Log.i(TAG,"Saved Selected")
                }
                R.id.profile ->{
                    setCurrentFragment(profileFragment)
                    Log.i(TAG,"Profile Selected")
                }
            }
            true
        }

    }
    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fl_wrapper,fragment)
            commit()
        }

}