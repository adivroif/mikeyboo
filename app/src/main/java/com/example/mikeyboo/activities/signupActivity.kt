package com.example.mikeyboo.activities

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mikeyboo.sharedData.DataManager
import com.example.mikeyboo.R
import com.example.mikeyboo.firebaseConnections.firebaseConnections
import com.example.mikeyboo.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson


class signupActivity : AppCompatActivity() {


    private lateinit var mikeyText: MaterialTextView

    private lateinit var address: MaterialTextView

    private lateinit var name: TextInputEditText

    private lateinit var mail: TextInputEditText

    private lateinit var phoneNumber: TextInputEditText

    private lateinit var userName: TextInputEditText

    private lateinit var password: TextInputEditText

    private lateinit var send_btn_signup: MaterialButton

    private var firebaseConnections = firebaseConnections()

    private var userRef = firebaseConnections.initDBConnection("user")

    private var dataManager = DataManager()

    private lateinit var token : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        findViews()
        initViews()

        dataManager.updateUsersFromDB()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result
            Log.d("TAG", token)
        })
    }

    private fun findViews() {
        mikeyText = findViewById(R.id.mikeyText)
        address = findViewById(R.id.address)
        name = findViewById(R.id.name)
        mail = findViewById(R.id.mail)
        phoneNumber = findViewById(R.id.phoneNumber)
        userName = findViewById(R.id.userName)
        password = findViewById(R.id.password)
        send_btn_signup = findViewById(R.id.send_btn_signup)
    }

    private fun initViews() {
        send_btn_signup.setOnClickListener { EditProfile() }
    }

    private fun EditProfile() {
        val listOfUsers = dataManager.getListOfUsers()
        var ok = checkData()
        if (!ok){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("הוספת לקוח !")
            builder.setMessage("הכנסת פרטים שגויים , נסה שוב")
            builder.setPositiveButton("אישור") { dialog, which ->
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            val dialog = builder.create()
            dialog.show()
        }

        val gson = Gson()
        var user =
            User(
                name.text.toString(),
                mail.text.toString(),
                phoneNumber.text.toString(),
                userName.text.toString(),
                password.text.toString(),
                token
            )
        val json = gson.toJson(user)
        if (ok) {
            listOfUsers.add(user)
            userRef.setValue(listOfUsers)
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("הוספת לקוח !")
        builder.setMessage("נוספת בהצלחה")
        builder.setPositiveButton("אישור") { dialog, which ->
            dataManager.updateTurnsFromDB(user)
            moveToLogin(json,user)
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onBackPressed() {
        supportFragmentManager.popBackStack()
            super.onBackPressed()

    }


    fun moveToLogin(json: String,user:User) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("user", json)
        startActivity(intent)
        finish()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun checkData(): Boolean {
        val ok: Boolean = checkDataOfUser(
            mail.text.toString(),
            phoneNumber.text.toString(),
            userName.text.toString(),
            password.text.toString()
        )
        if (!ok) {
            return false
        } else
            return true
    }

    fun checkDataOfUser(
        mail: String,
        phoneNumber: String,
        userName: String,
        password: String
    ): Boolean {
        for (i in 0..<dataManager.getList().size) {
            if (dataManager.getList()[i].mail == mail || dataManager.getList()[i].phoneNumber == phoneNumber || dataManager.getList()[i].userName == userName)
                return false
        }
        if (!checkMail(mail))
            return false
        if (!checkPhoneNumber(phoneNumber))
            return false
        if (!checkPassword(password))
            return false
        return true
    }

    private fun checkPassword(password: String): Boolean {
        if (isValidPassword(password)) {
            return true
        } else {
            Toast.makeText(this, "Wrong password", Toast.LENGTH_LONG).show()
            return false
        }
    }

    private fun checkPhoneNumber(phoneNumber: String): Boolean {
        if (isValidIsraeliPhoneNumber(phoneNumber)) {
            return true
        } else {
            Toast.makeText(this, "Wrong phone number", Toast.LENGTH_LONG).show()
            return false
        }
    }

    private fun checkMail(mail: String): Boolean {
        val emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com$"
        if (mail.matches(emailPattern.toRegex())) {
            if (mail.endsWith(".com"))
                return true
        } else {
            Toast.makeText(this, "Wrong mail", Toast.LENGTH_LONG).show()
            return false
        }
        return false
    }


    fun isValidIsraeliPhoneNumber(phoneNumber: String): Boolean {
        if (android.util.Patterns.PHONE.matcher(phoneNumber).matches())
            return true
        else
            return false
    }

    fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
        return password.matches(passwordPattern.toRegex())
    }
}