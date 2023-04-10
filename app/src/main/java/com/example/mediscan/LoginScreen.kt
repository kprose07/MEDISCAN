package com.example.mediscan

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mediscan.databinding.ActivityLoginScreenBinding
import com.example.mediscan.databinding.ActivityRegisterScreenBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login_screen.*
import kotlinx.android.synthetic.main.activity_login_screen.login_button

@Suppress("DEPRECATION")
class LoginScreen : AppCompatActivity() {

    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signupUpPrompt.setOnClickListener {
            val intent = Intent(this, RegisterScreen::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener{
            val email = binding.loginEmailInput.text.toString()
            val password = binding.loginPwInput.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){
                        val intent = Intent(this, MainActivity::class.java)
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
        }

        binding.forgotpw.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Forgot Password")
            val view = layoutInflater.inflate(R.layout.dialog_forgot_password,null)
            val emailfpw: TextInputEditText = view.findViewById<TextInputEditText>(R.id.forgot_email_input)

            builder.setView(view)
            builder.setPositiveButton("Reset", DialogInterface.OnClickListener { _, _ ->
                forgotPassword(emailfpw)
            })
            builder.setNegativeButton("Close", DialogInterface.OnClickListener { _, _ ->  })
                builder.show()
        }

        }

    private fun forgotPassword(emailfpw: EditText){
        if(emailfpw.text.toString().isEmpty()){
            Toast.makeText(
                this,
                "Please insert an email",
                Toast.LENGTH_SHORT).show()
          return
        }

        Firebase.auth.sendPasswordResetEmail(emailfpw.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Email sent",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
//    override fun onStart(){
//        super.onStart()
//        if(firebaseAuth.currentUser != null){
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }
//    }
    }
