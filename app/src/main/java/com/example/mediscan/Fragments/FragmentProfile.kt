package com.example.mediscan.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mediscan.Adapter.ProfileRemindAdapter
import com.example.mediscan.Adapter.RecentsAdapter
import com.example.mediscan.Data.Communicator
import com.example.mediscan.Data.ProfileRemind
import com.example.mediscan.Data.Recents
import com.example.mediscan.LoginScreen
import com.example.mediscan.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*

import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_saved.*
import java.util.*

class ProfileFragment : Fragment() {
    val remindList = ArrayList<ProfileRemind>()

    private lateinit var database : DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var comm: Communicator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        val view = inflater.inflate(R.layout.fragment_profile, container,false)
        comm = requireActivity() as Communicator
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profile_reminder.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        profile_reminder.adapter = ProfileRemindAdapter(remindList, remind_popupcard, spinner_remind_meds, remind_close)
        reminddata()
      //  val profTitle: String = profile_name_input.text.toString()
            //val user = Firebase.auth.currentUser.toString()
           // readData("kprose07")
            firebaseAuth = FirebaseAuth.getInstance()
            loaduserinfo()
        profile_emailLink.setOnClickListener{
            v:View -> comm.openEmailClient()
        }


        edit_p.setOnClickListener{
            val fragment = EditProfileFragment()
            val fragmentManager = fragmentManager
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.replace(R.id.fl_wrapper, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        profile_sign_out.setOnClickListener {
            FirebaseAuth.getInstance().signOut();
            val intent = Intent(context, LoginScreen::class.java)
            startActivity(intent)
        }

    }

    private fun loaduserinfo() {
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot){
                    //user info
                    val firstName ="${snapshot.child("firstName").value}"
                    val email ="${snapshot.child("email").value}"
                    val phone ="${snapshot.child("phone").value}"
                    val dob ="${snapshot.child("dob").value}"
                    //val uid ="${snapshot.child("uid").value}"
                    val pimg ="${snapshot.child("profileImage").value}"

                    Log.i("pimgname",pimg)
                    //val formatDate = MyApplication.formatTimeStamp(timestamp.toLong())
                    profile_name_input.text = firstName
                    profile_email_input.text = email
                    profile_dob_input.text = dob
                    profile_phone_input.text = phone

                    try {
                        Glide.with(this@ProfileFragment).load(pimg).placeholder(R.drawable.prof).into(profile_img)
                    }catch (e: Exception){

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
//
//    private fun readData(userName: String) {
////        database = FirebaseDatabase.getInstance().getReference("users")
////        database.child(userName).get().addOnSuccessListener {
////            if(it.exists()){
////                val firstName = it.child("firstName").value
////                val phone = it.child("phone").value
////                val email = it.child("email" +
////                        "").value
////                profile_name_input.text = firstName.toString()
////                profile_phone_input.text = phone.toString()
////                profile_email_input.text = email.toString()
////            }
////        }
//    }

    private fun reminddata(){
        remindList.add(
            ProfileRemind(
                "Rose",
                1,
                false

            )
        )
        remindList.add(
            ProfileRemind(
                "Humilin",
                1,
                true

            )
        )
        remindList.add(
            ProfileRemind(
                "Humilin",
                1,
                true

            )
        )
    }
}