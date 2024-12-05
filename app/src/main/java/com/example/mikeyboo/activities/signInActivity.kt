package com.example.mikeyboo.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.mikeyboo.sharedData.DataManager
import com.example.mikeyboo.R
import com.example.mikeyboo.models.User
import com.example.mikeyboo.models.listOfUsers
import com.example.mikeyboo.sharedData.SharedViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.gson.Gson

class signInActivity : AppCompatActivity() {


    private lateinit var mikeyText: MaterialTextView

    private lateinit var address: MaterialTextView

    private lateinit var userName: EditText

    private lateinit var password: EditText

    private lateinit var send_btn_signin: MaterialButton

    private var dataManager = DataManager()

    private var user :User?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        findViews()
        initViews()
        dataManager.updateUsersFromDB()
        dataManager.updateTypesFromDB()
        dataManager.updateReviewsFromDB()

    }

    private fun findViews() {
        mikeyText = findViewById(R.id.mikeyText)
        address = findViewById(R.id.address)
        userName = findViewById(R.id.userName)
        password = findViewById(R.id.password)
        send_btn_signin = findViewById(R.id.send_btn_signin)

    }

    private fun initViews() {
        send_btn_signin.setOnClickListener { EditProfile()}
    }

    @SuppressLint("SuspiciousIndentation")
    private fun EditProfile() {
        val gson = Gson()
        val user: User? = searchUser()
        dataManager.setUser(user)
        if (user == null) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("הוספת לקוח !")
            builder.setMessage("שם מתשמש/סיסמא שגויים , נסי שוב")
            builder.setPositiveButton("אישור") { dialog, which ->
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            val dialog = builder.create()
            dialog.show()
        }
        this.user = user
        SharedViewModel().setUser(user)
        if (user != null) {
            dataManager.updateTurnsFromDB(user)
            dataManager.updatePhotosFromDB()
        }
        val json = gson.toJson(user)
        var intent: Intent
        if (user?.userName == "michaellevi") {
            intent = Intent(this, BusinessSideActivity::class.java)
            intent.putExtra("user from login", json)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
            finish()
            return
        } else {
            //dataManager.initialUserTurns(dataManager.getListOfTurns())
            intent = Intent(this, OptionsActivty::class.java)
            intent.putExtra("user from login", json)
            startActivity(intent)
            finish()
        }
    }

    private fun searchUser(): User? {
        var user: User? = null
        var ok: Boolean = false
        while (!ok) {
            user = dataManager.getUser(userName.text.toString(), password.text.toString())
            if (user == null) {
                refresh()
            } else
                ok = true
        }

        return user!!
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }



    private fun refresh() {
        val intent = Intent(this, signupActivity::class.java)
        val b = Bundle()
        intent.putExtras(b)
        startActivity(intent)
        finish()
    }
}
