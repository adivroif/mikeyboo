package com.example.mikeyboo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class signInActivity : AppCompatActivity() {


    private lateinit var mikeyText : MaterialTextView

    private lateinit var address : MaterialTextView

    private lateinit var instagram_icon : ShapeableImageView

    private lateinit var whatsapp_icon : ShapeableImageView

    private lateinit var phone_icon : ShapeableImageView

    private lateinit var googleMap_icon : ShapeableImageView

    private lateinit var userName : EditText

    private lateinit var password : EditText

    private lateinit var send_btn_signin : MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        findViews()
        initViews()
    }

    private fun initViews() {
        instagram_icon.setOnClickListener {MoveToInstagram()}
        whatsapp_icon.setOnClickListener {MoveToWhatsapp()}
        phone_icon.setOnClickListener {MoveToPhone()}
        googleMap_icon.setOnClickListener {MoveToGoogleMaps()}
        send_btn_signin.setOnClickListener {EditProfile()}
    }

    private fun EditProfile() {
        val gson = Gson()
        val user : User = searchUser()
        DataManager().addUser(user)
        val json = gson.toJson(user)
        var intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user", json)
        startActivity(intent)
        finish()
    }

    private fun searchUser() : User {
        var user: User? = null
        var ok : Boolean = false
        lifecycleScope.launch {
            while (!ok) {
                user = DataManager().getUser(userName.toString(), password.toString())
                if (user == null) {
                    //TODO(toast)
                    delay(3000L)
                    refresh()
                }
                else
                    ok = true
            }
        }
        return user!!
    }

    private fun refresh() {
        val intent = Intent(this, signupActivity::class.java)
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
        userName = findViewById(R.id.userName)
        password = findViewById(R.id.password)
        send_btn_signin = findViewById(R.id.send_btn_signin)

    }
}