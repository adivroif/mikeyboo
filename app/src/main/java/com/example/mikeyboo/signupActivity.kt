package com.example.mikeyboo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch


class signupActivity : AppCompatActivity() {


    private lateinit var mikeyText : MaterialTextView

    private lateinit var address : MaterialTextView

    private lateinit var instagram_icon : ShapeableImageView

    private lateinit var whatsapp_icon : ShapeableImageView

    private lateinit var phone_icon : ShapeableImageView

    private lateinit var googleMap_icon : ShapeableImageView

    private lateinit var name : TextInputEditText

    private lateinit var mail : TextInputEditText

    private lateinit var phoneNumber : TextInputEditText

    private lateinit var userName : TextInputEditText

    private lateinit var password : TextInputEditText

    private lateinit var send_btn_signup : MaterialButton

    private lateinit var life : Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        findViews()
        initViews()

    }

    private fun findViews() {
        mikeyText = findViewById(R.id.mikeyText)
        address = findViewById(R.id.address)
        instagram_icon = findViewById(R.id.instagram_icon)
        whatsapp_icon = findViewById(R.id.whatsapp_icon)
        phone_icon = findViewById(R.id.phone_icon)
        googleMap_icon = findViewById(R.id.googleMap_icon)
        name = findViewById(R.id.name)
        mail = findViewById(R.id.mail)
        phoneNumber = findViewById(R.id.phoneNumber)
        userName = findViewById(R.id.userName)
        password = findViewById(R.id.password)
        send_btn_signup = findViewById(R.id.send_btn_signup)
    }

    private fun initViews() {
        instagram_icon.setOnClickListener {MoveToInstagram()}
        whatsapp_icon.setOnClickListener {MoveToWhatsapp()}
        phone_icon.setOnClickListener {MoveToPhone()}
        googleMap_icon.setOnClickListener {MoveToGoogleMaps()}
        send_btn_signup.setOnClickListener {EditProfile()}
    }

    private fun EditProfile() {
        checkData()
        val gson = Gson()
        var user =
            User(
                name.text.toString(),
            mail.text.toString(),
            phoneNumber.text.toString(),
            userName.text.toString(),
            password.text.toString())

        DataManager().addUser(user)
        val json = gson.toJson(user)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user", json)
        startActivity(intent)
        finish()
    }

    private fun checkData() {
        var ok: Boolean = false

        life = lifecycleScope.launch {
            (Dispatchers.IO){
                while (!ok) {
                    ok = DataManager().checkDataOfUser()
                    if (!ok) {
                        //TODO(toast)
                        delay(3000L)
                        life.cancel()
                        refresh()
                    }
                }
            }
        }
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
}