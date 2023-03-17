package com.example.mediscan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.mediscan.Fragments.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val tag = "MainActivity"

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
                    Log.i(tag,"Home Selected")
                }
                R.id.pills ->{
                    setCurrentFragment(pillsdFragment)
                    Log.i(tag,"Pills Selected")
                }
                R.id.saved ->{
                    setCurrentFragment(savedFragment)
                    Log.i(tag,"Saved Selected")
                }
                R.id.profile ->{
                    setCurrentFragment(profileFragment)
                    Log.i(tag,"Profile Selected")
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