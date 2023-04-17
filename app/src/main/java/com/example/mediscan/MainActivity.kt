package com.example.mediscan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mediscan.Data.Communicator
import com.example.mediscan.Data.NarrowDownSearch
import com.example.mediscan.Fragments.*
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_main.*


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

    override fun passDataCom(medicineSelected: String, medicineId: String, brandName: String) {
        val bundle = Bundle()
        bundle.putString("mdSelected", medicineSelected)
        bundle.putString("mdId", medicineId)
        bundle.putString("brandName", brandName)
        val transaction = this.supportFragmentManager.beginTransaction()
        val frag2 = PillsFragment()
        frag2.arguments = bundle

        transaction.replace(R.id.fl_wrapper, frag2)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }

    override fun openResultsPage(
        narrowDownSearch: List<NarrowDownSearch>,
        itemClicked: String,
        medicineName: String,
        brandName: String
    ) {

        val bundle = Bundle()

        bundle.putParcelableArrayList("narrowData", ArrayList(narrowDownSearch))
        bundle.putString("medicineName", medicineName)
        bundle.putString("itemClicked", itemClicked)
        bundle.putString("brandName", brandName)
        val transaction = this.supportFragmentManager.beginTransaction()
        val resultsFragment = ResultsFragment()
        resultsFragment.arguments = bundle

        transaction.replace(R.id.fl_wrapper, resultsFragment)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }

    override fun deleteFromDB(db: DatabaseReference, id: String) {
        db.child(id).removeValue()
    }

    override fun openEmailClient() {
        val i = Intent(Intent.ACTION_SEND)
        i.data = Uri.parse("email")
        val s = arrayOf("inquiries@mediscan.com")
        i.putExtra(Intent.EXTRA_EMAIL, s)
        i.putExtra(Intent.EXTRA_SUBJECT, "Medical Inquiry")
        i.putExtra(Intent.EXTRA_TEXT, "Hi, Enter your inquiry here")
        i.type = "message/rfc822"
        val chooser = Intent.createChooser(i, "Launch Email")
        startActivity(chooser)

    }


}