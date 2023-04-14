package com.example.mediscan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mediscan.Data.Communicator
import com.example.mediscan.Fragments.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_pillsd.*

class MainActivity : AppCompatActivity(), Communicator {
    val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Diffrent Screens
        val homeFragment = HomeFragment()
        val pillsdFragment = PillsFragment()
        val savedFragment = SavedFragment()
        val profileFragment = ProfileFragment()
        val editProfile = EditProfileFragment()
        val resultsFragment = ResultsFragment()
       // val loginFragment = LoginFragment()

        //Sets Home Screen
        setCurrentFragment(homeFragment)

        //Bottom Navigation
        bottom_nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home ->{
                    setCurrentFragment(homeFragment)
                    Log.i(tag,"Home Selected")
                }
//                R.id.pills ->{
//                    setCurrentFragment(pillsdFragment)
//                    Log.i(tag,"Pills Selected")
//                }
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

    override fun passDataCom(medicineSelected: String, medicineId: String) {
        val bundle = Bundle()
        bundle.putString("mdSelected", medicineSelected)
        bundle.putString("mdId", medicineId)
        val transaction = this.supportFragmentManager.beginTransaction()
        val frag2 = PillsFragment()
        frag2.arguments = bundle

        transaction.replace(R.id.fl_wrapper, frag2)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }


}