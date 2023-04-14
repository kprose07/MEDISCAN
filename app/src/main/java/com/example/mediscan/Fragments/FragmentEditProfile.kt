package com.example.mediscan.Fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediscan.Adapter.ProfileRemindAdapter
import com.example.mediscan.Adapter.RecentsAdapter
import com.example.mediscan.Data.ProfileRemind
import com.example.mediscan.Data.Recents
import com.example.mediscan.LoginScreen
import com.example.mediscan.R
import com.example.mediscan.databinding.ActivityLoginScreenBinding
import com.example.mediscan.databinding.ActivityRegisterScreenBinding
import com.example.mediscan.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_home.*

import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_saved.*

class EditProfileFragment : Fragment() {

   // private lateinit var binding: FragmentEditProfileBinding
    private lateinit var database : DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    val profileFragment = ProfileFragment()
    //private lateinit var progressDialog: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_edit_profile, container,false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        edit_button.setOnClickListener{
            validateData()

        }

    }
    private var email = ""
    private var firstName = ""
    private var lastName = ""
    private var phone = ""
    private var dob = ""
    private fun validateData() {
        email = edit_email_input.text.toString().trim()
        firstName = edit_first_name_input.text.toString().trim()
        lastName = edit_last_name_input.text.toString().trim()
        phone = edit_phone_input.text.toString().trim()
        dob = edit_dob_input.text.toString().trim()

        //Validate Data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(
                context,
                "Invalid email",
                Toast.LENGTH_SHORT).show()
        }else if(firstName.isEmpty()||lastName.isEmpty()||phone.isEmpty()||dob.isEmpty()){
            Toast.makeText(
                context,
                "Please fill in all fields.",
                Toast.LENGTH_SHORT).show()
        }else{
            updateUserInfo()
        }
    }
    private fun updateUserInfo() {
        //save user info
        val timeStamp = System.currentTimeMillis()

        //get current user id
        val uid = firebaseAuth.uid
        val email = edit_email_input.text.toString()
        val firstName = edit_first_name_input.text.toString()
        val lastName = edit_last_name_input.text.toString()
        val dob = edit_dob_input.text.toString()
        val phone = edit_phone_input.text.toString()


        //setup data to add in db
        val hashmap: HashMap<String, Any?> = HashMap()
        hashmap["email"] = email
        hashmap["firstName"] = firstName
        hashmap["lastName"] = lastName
        hashmap["phone"] = phone
        hashmap["dob"] = dob
        hashmap["profileImage"] = "" //add manully later
        hashmap["timestamp"] = timeStamp

        //set data in db
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(firebaseAuth.uid!!)
            .updateChildren(hashmap)
            .addOnSuccessListener {
                //user info saved
                Toast.makeText(
                    context,
                    "User info saved",
                    Toast.LENGTH_SHORT).show()

                loadProfile()



            }
            .addOnFailureListener{ e->

                Toast.makeText(
                    context,
                    "Failed saving User Info due to ${e.message}",
                    Toast.LENGTH_SHORT).show()
            }

    }
    fun loadProfile(){
        val fragment = ProfileFragment()
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.fl_wrapper, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}