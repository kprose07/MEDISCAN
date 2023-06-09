package com.example.mediscan

import android.R.attr.phoneNumber
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mediscan.databinding.ActivityRegisterScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register_screen.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


@Suppress("DEPRECATION")
class RegisterScreen : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth
    //private lateinit var database : DatabaseReference
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        //init progress bar
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wiat")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.loginPrompt.setOnClickListener {
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
            finish()
        }

        binding.registerButton.setOnClickListener{
           validateData()
        }
    }

    private var email = ""
    private var password = ""
    private var firstName = ""
    private var lastName = ""
    private var phone = ""
    private var dob = ""

    private fun validateData() {
        //Input Data
        email = binding.regEmailInput.text.toString().trim()
        password = binding.regPwInput.text.toString().trim()
        firstName = binding.regFirstNameInput.text.toString().trim()
        lastName = binding.regLastNameInput.text.toString().trim()
        dob = binding.regDobInput.text.toString().trim()
        phone = binding.regPhoneInput.text.toString().trim()

        //Validate Data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(
                this,
                "Invalid email",
                Toast.LENGTH_SHORT).show()
        }else if(password.isEmpty()){
            Toast.makeText(
                this,
                "Please Enter Password",
                Toast.LENGTH_SHORT).show()
        }else{
            createUserAccount()
        }

    }

    private fun createUserAccount() {
        //Create User Account

        //create user in firebase auth
        progressDialog.setMessage("Creating Acount")
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //account created
                updateUserInfo()

            }
            .addOnFailureListener{ e->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Failed creating account due to ${e.message}",
                    Toast.LENGTH_SHORT).show()
            }


    }

    private fun updateUserInfo() {
        //save user info
        progressDialog.setMessage("Saving User Info...")
        val timeStamp = System.currentTimeMillis()

        //get current user id
        val uid = firebaseAuth.uid



        //setup data to add in db
        val hashmap: HashMap<String, Any?> = HashMap()
        hashmap["uid"] = uid
        hashmap["email"] = email
        hashmap["firstName"] = firstName
        hashmap["lastName"] = lastName
        hashmap["phone"] = phone
        hashmap["dob"] = dob
        hashmap["profileImage"] = "" //add manully later
        hashmap["timestamp"] = timeStamp

        //set data in db
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(uid!!)
            .setValue(hashmap)
            .addOnSuccessListener {
                //user info saved
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Acount Created...",
                    Toast.LENGTH_SHORT).show()

                startActivity(Intent(this@RegisterScreen,LoginScreen::class.java))
                finish()

            }
            .addOnFailureListener{ e->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Failed saving User Info due to ${e.message}",
                    Toast.LENGTH_SHORT).show()
            }

    }



    }




