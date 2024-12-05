package com.example.mikeyboo.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mikeyboo.R
import com.example.mikeyboo.sharedData.DataManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class LoginActivity: AppCompatActivity() {

    private lateinit var mikeyText: MaterialTextView

    private lateinit var address: MaterialTextView

    private lateinit var instagram_icon: ShapeableImageView

    private lateinit var whatsapp_icon: ShapeableImageView

    private lateinit var phone_icon: ShapeableImageView

    private lateinit var googleMap_icon: ShapeableImageView

    private lateinit var signup: MaterialButton

    private lateinit var signin: MaterialButton

    private var dataManager = DataManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
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
        signup = findViewById(R.id.signup)
        signin = findViewById(R.id.signin)
    }

    private fun initViews() {
        instagram_icon.setOnClickListener { MoveToInstagram() }
        whatsapp_icon.setOnClickListener { MoveToWhatsapp() }
        phone_icon.setOnClickListener { MoveToPhone() }
        googleMap_icon.setOnClickListener { MoveToGoogleMaps() }
        signup.setOnClickListener { MoveToSignUp() }
        signin.setOnClickListener { MoveToSignIn() }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }


    private fun MoveToSignIn() {
        val intent = Intent(this, signInActivity::class.java)
        val b = Bundle()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtras(b)
        startActivity(intent)
    }

    private fun MoveToSignUp() {
        val intent = Intent(this, signupActivity::class.java)
        val b = Bundle()
        intent.putExtras(b)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun MoveToGoogleMaps() {
        val latitude = 32.0853 // החלף בקו רוחב הרצוי
        val longitude = 34.7818 // החלף בקו אורך הרצוי
        val address = "רחוב דיזנגוף 100, תל אביב" // החלף בכתובת הרצויה

        try {
            // ניסיון פתיחת Waze עם מיקום
            val url = "https://waze.com/ul?ll=$latitude,$longitude&navigate=yes"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            try {
                // ניסיון פתיחת Waze עם כתובת
                val url = "https://waze.com/ul?q=$address"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // טיפול במקרה ש-Waze לא מותקן במכשיר
            }
        }

    }

    private fun MoveToPhone() {
        val phoneNumber = "+972545250413" // החלף במספר הטלפון הרצוי

        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // טיפול במקרה שאפליקציית החייגן לא נמצאה
        }
    }

    private fun MoveToWhatsapp() {
        val phoneNumber = "+972545250413" // החלף במספר הטלפון הרצוי
        val message = "שלום!" // החלףבהודעה הרצויה

        val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // טיפול במקרה ש-WhatsApp לא מותקן במכשיר
        }
    }

    private fun MoveToInstagram() {
        val username = "mikeyboo__" // החלף בשם המשתמש הרצוי

        val uri = Uri.parse("http://instagram.com/_u/$username")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        intent.setPackage("com.instagram.android") // הגדרת חבילת Instagram

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // טיפול במקרה ש-Instagram לא מותקן במכשיר
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/$username")))
        }
    }
}