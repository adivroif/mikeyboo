package com.example.mikeyboo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class LoginActivity: AppCompatActivity() {

    private lateinit var mikeyText : MaterialTextView

    private lateinit var address : MaterialTextView

    private lateinit var instagram_icon : ShapeableImageView

    private lateinit var whatsapp_icon : ShapeableImageView

    private lateinit var phone_icon : ShapeableImageView

    private lateinit var googleMap_icon : ShapeableImageView

    private lateinit var signup : MaterialButton

    private lateinit var signin : MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        findViews()
        initViews()
    }

    private fun initViews() {
        instagram_icon.setOnClickListener {MoveToInstagram()}
        whatsapp_icon.setOnClickListener {MoveToWhatsapp()}
        phone_icon.setOnClickListener {MoveToPhone()}
        googleMap_icon.setOnClickListener {MoveToGoogleMaps()}
        signup.setOnClickListener {MoveToSignUp()}
        signin.setOnClickListener {MoveToSignIn()}
    }

    private fun MoveToSignIn() {
        val intent = Intent(this, signInActivity::class.java)
        val b = Bundle()
        intent.putExtras(b)
        startActivity(intent)
        finish()
    }

    private fun MoveToSignUp() {
        val intent = Intent(this, signupActivity::class.java)
        val b = Bundle()
        intent.putExtras(b)
        startActivity(intent)
        finish()
    }

    private fun MoveToGoogleMaps() {
        TODO("Not yet implemented")
    }

    private fun MoveToPhone() {
        TODO("Not yet implemented")
    }

    private fun MoveToWhatsapp() {
        TODO("Not yet implemented")
    }

    private fun MoveToInstagram() {
        TODO("Not yet implemented")
    }

    private fun findViews() {
        mikeyText = findViewById(R.id.mikeyText)
        address = findViewById(R.id.address)
        instagram_icon = findViewById(R.id.instagram_icon)
        whatsapp_icon = findViewById(R.id.whatsapp_icon)
        phone_icon = findViewById(R.id.phone_icon)
        googleMap_icon = findViewById(R.id.googleMap_icon)
        signup = findViewById(R.id.signup)
        signin = findViewById(R.id.signin)
    }
}