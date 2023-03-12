package com.example.mediscan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        // Handler().postDelayed({
        Handler(Looper.getMainLooper()).postDelayed({
            setContentView(R.layout.activity_main)
        }, 3000)
    }

}