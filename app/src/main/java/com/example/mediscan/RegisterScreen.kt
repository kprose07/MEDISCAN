package com.example.mediscan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mediscan.Data.User
import com.example.mediscan.databinding.ActivityRegisterScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register_screen.*
import kotlinx.android.synthetic.main.activity_register_screen.login_prompt

@Suppress("DEPRECATION")
class RegisterScreen : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.loginPrompt.setOnClickListener {
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
        }

        binding.registerButton.setOnClickListener{
            val userName = binding.userUsernameRegInput.text.toString()
            val firstName = binding.regFirstNameInput.text.toString()
            val lastName = binding.regLastNameInput.text.toString()
            val email = binding.regEmailInput.text.toString()
            val password = binding.regPwInput.text.toString()
            val dob = binding.regDobInput.text.toString()
            val phone = binding.regPhoneInput.text.toString()

            if(firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && dob.isNotEmpty() && phone.isNotEmpty()){
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){
                        val intent = Intent(this, LoginScreen::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(
                            this,
                            it.exception.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(
                    this,
                    "Please fill in ALL fields",
                    Toast.LENGTH_SHORT).show()
            }

            database = FirebaseDatabase.getInstance().getReference("users")
            val User = User(userName,firstName,lastName, email, password,dob,phone)
            database.child(userName).setValue(User).addOnSuccessListener {
                binding.userUsernameRegInput.text?.clear()
                binding.regFirstNameInput.text?.clear()
                binding.regLastNameInput.text?.clear()
                binding.regEmailInput.text?.clear()
                binding.regPwInput.text?.clear()
                binding.regDobInput.text?.clear()
                binding.regPhoneInput.text?.clear()
                Toast.makeText(
                    this,
                    "Saved",
                    Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Failed",
                    Toast.LENGTH_SHORT).show()
            }



        }
    }



}

