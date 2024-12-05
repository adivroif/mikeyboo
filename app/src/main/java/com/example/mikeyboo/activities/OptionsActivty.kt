package com.example.mikeyboo.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mikeyboo.R
import com.example.mikeyboo.databinding.ActivityOptionsActivtyBinding
import com.example.mikeyboo.firebaseConnections.firebaseConnections
import com.example.mikeyboo.fragments_of_customer_side.HistoryFragment
import com.example.mikeyboo.models.Turn
import com.example.mikeyboo.models.User
import com.example.mikeyboo.models.waitListsDB
import com.example.mikeyboo.sharedData.DataManager
import com.example.mikeyboo.sharedData.SharedViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import java.time.LocalDate
import java.time.LocalTime

class OptionsActivty : AppCompatActivity() {

    private lateinit var mikeyText: MaterialTextView

    private lateinit var cancel_turn: MaterialButton

    private lateinit var change_turn: MaterialButton

    private lateinit var main_LST_scores: MaterialTextView

    private lateinit var address: MaterialTextView

    private lateinit var instagram_icon: ShapeableImageView

    private lateinit var whatsapp_icon: ShapeableImageView

    private lateinit var phone_icon: ShapeableImageView

    private lateinit var googleMap_icon: ShapeableImageView

    private lateinit var worksText: MaterialTextView

    private lateinit var what_do_today_text: MaterialTextView

    private lateinit var lak_gel: MaterialButton

    private lateinit var list_of_options3: LinearLayout

    private lateinit var milui: MaterialButton

    private lateinit var menikur: MaterialButton

    private lateinit var tikun: MaterialButton

    private lateinit var hasara: MaterialButton

    private lateinit var tzior: MaterialButton

    private lateinit var bnia_hadash: MaterialButton

    private lateinit var history_fragment: HistoryFragment

    private var firebaseConnections = firebaseConnections()

    private var turnsRef = firebaseConnections.initDBConnection("turns")

    private var waitListRef = firebaseConnections.initDBConnection("waitList")

    private lateinit var database: FirebaseDatabase

    private var dataManager = DataManager()

    private var listOfTurns = ArrayList<Turn>()

    private var listOfTurnsFromDB = ArrayList<HashMap<String, Int>>()

    private var listOfWaitList = ArrayList<waitListsDB>()

    private var user: User? = null

    private val viewModel: SharedViewModel by viewModels()

    private var numOfUsers: Int = 0

    private var closeTurn: Turn? = null

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var binding: ActivityOptionsActivtyBinding

    private val listOfButton: ArrayList<MaterialButton> = ArrayList()

    private var listOfType = dataManager.getListOfTypes()


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("On Create options", listOfTurns.size.toString())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            )
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
        }

        val topic = "highScores"
        binding = ActivityOptionsActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarOptionsActivty.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navViewCustomer
        val navController = findNavController(R.id.nav_host_fragment_content_options_activty)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.WaitListFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        findViews()
        initViews()
        cancel_turn.visibility = View.INVISIBLE
        change_turn.visibility = View.INVISIBLE
        var getFromLogin: String? = intent.getStringExtra("user from login")
        var getFromCalendar: String? = intent.getStringExtra("user from calendar")
        var getFromHomeFragment: String? = intent.getStringExtra("user from homeFragment")
        var yes = intent.getStringExtra("yes")

        if (getFromLogin != null) {
            Log.d("Get From Login Options", getFromLogin)
        }
        if (getFromCalendar != null) {
            Log.d("Get From Calendar Options", getFromCalendar)
        }
        if (getFromCalendar != null) {
            user = User(
                getFromCalendar.split(",")[1].split(":")[1].split(" ")[0],
                getFromCalendar.split(",")[0].split(":")[1].split(" ")[0],
                getFromCalendar.split(",")[3].split(":")[1].split(" ")[0],
                getFromCalendar.split(",")[4].split(":")[1].split(" ")[0],
                getFromCalendar.split(",")[2].split(":")[1].split(" ")[0],
                getFromCalendar.split(",")[5].split(":")[1].split(" ")[0]
            )
            user = User(
                user!!.name.substring(1, user!!.name.length-1),
                user!!.mail.substring(1, user!!.mail.length - 1),
                user!!.phoneNumber.substring(1, user!!.phoneNumber.length - 1),
                user!!.userName.substring(1, user!!.userName.length - 2),
                user!!.password.substring(1, user!!.password.length - 1),
                user!!.token.substring(1, user!!.token.length - 1)
            )
        } else if (getFromLogin != null) {
            user = User(
                getFromLogin.split(",")[1].split(":")[1].split(" ")[0],
                getFromLogin.split(",")[0].split(":")[1].split(" ")[0],
                getFromLogin.split(",")[3].split(":")[1].split(" ")[0],
                getFromLogin.split(",")[5].split(":")[1].split(" ")[0],
                getFromLogin.split(",")[2].split(":")[1].split(" ")[0],
                getFromLogin.split(",")[4].split(":")[1].split(" ")[0]
            )
            user = User(
                user!!.name.substring(1, user!!.name.length),
                user!!.mail.substring(1, user!!.mail.length - 1),
                user!!.phoneNumber.substring(1, user!!.phoneNumber.length - 1),
                user!!.userName.substring(1, user!!.userName.length - 2),
                user!!.password.substring(1, user!!.password.length - 1),
                user!!.token.substring(1, user!!.token.length - 1)
            )

            dataManager.setUser(user)

            listOfButton.add(lak_gel)
            listOfButton.add(milui)
            listOfButton.add(menikur)
            listOfButton.add(tzior)
            listOfButton.add(hasara)
            listOfButton.add(tikun)
            listOfButton.add(bnia_hadash)

            for (i in 0..<listOfButton.size) {
                if (listOfType.size > i)
                    listOfButton[i].text =
                        listOfType[i].type + " - " + listOfType[i].price.toString() + "₪"
                else
                    listOfButton[i].visibility = View.INVISIBLE
            }
        }
        else if (getFromHomeFragment != null) {
                user = User(
                    getFromHomeFragment.split(",")[1].split(":")[1].split(" ")[0],
                    getFromHomeFragment.split(",")[0].split(":")[1].split(" ")[0],
                    getFromHomeFragment.split(",")[3].split(":")[1].split(" ")[0],
                    getFromHomeFragment.split(",")[4].split(":")[1].split(" ")[0],
                    getFromHomeFragment.split(",")[2].split(":")[1].split(" ")[0],
                    getFromHomeFragment.split(",")[5].split(":")[1].split(" ")[0]
                )

            user = User(
                user!!.name.substring(1, user!!.name.length-1),
                user!!.mail.substring(1, user!!.mail.length - 1),
                user!!.phoneNumber.substring(1, user!!.phoneNumber.length - 1),
                user!!.userName.substring(1, user!!.userName.length - 2),
                user!!.password.substring(1, user!!.password.length - 1),
                user!!.token.substring(1, user!!.token.length - 1)
            )
        }
        user = dataManager.getUser()
        SharedViewModel().setUser(user)
        val gson = Gson()
        val json = gson.toJson(user)
        SharedViewModel().setData(json)
        Log.d("Shared Data", SharedViewModel().getData()!!)
        mikeyText.text = "היי " + user!!.name + ", "
        numOfUsers = DataManager().getNumOfUsers()
        dataManager.updateWorkHoursFromDB()
        //dataManager.updateTurnsFromDB(user!!)
        dataManager.updateWaitListFromDB()
        dataManager.updateUsersFromDB()
        dataManager.updateTypesFromDB()
        this.loadDat()
        setFragment()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private fun setFragment() {
        history_fragment = HistoryFragment()
        if (listOfTurns.isNotEmpty()) {
            history_fragment.setPlayerList(listOfTurns)
        }
    }

    private fun findViews() {
        mikeyText = findViewById(R.id.mikeyText)
        cancel_turn = findViewById(R.id.cancel_turn)
        change_turn = findViewById(R.id.change_turn)
        main_LST_scores = findViewById(R.id.main_LST_scores)
        instagram_icon = findViewById(R.id.instagram_icon)
        whatsapp_icon = findViewById(R.id.whatsapp_icon)
        phone_icon = findViewById(R.id.phone_icon)
        googleMap_icon = findViewById(R.id.googleMap_icon)
        worksText = findViewById(R.id.worksText)
        what_do_today_text = findViewById(R.id.what_do_today_text)
        lak_gel = findViewById(R.id.lak_gel)
        milui = findViewById(R.id.milui)
        menikur = findViewById(R.id.menikur)
        tikun = findViewById(R.id.tikun)
        hasara = findViewById(R.id.hasara)
        tzior = findViewById(R.id.tzior)
        bnia_hadash = findViewById(R.id.bnia_hadash)
        list_of_options3 = findViewById(R.id.list_of_options3)
        //fragment_history_turns = findViewById(R.id.fragment_history_turns)
    }

    private fun initViews() {
        instagram_icon.setOnClickListener { MoveToInstagram() }
        whatsapp_icon.setOnClickListener { MoveToWhatsapp() }
        phone_icon.setOnClickListener { MoveToPhone() }
        googleMap_icon.setOnClickListener { MoveToWaze() }
        lak_gel.setOnClickListener { OpenCalendar(lak_gel) }
        milui.setOnClickListener { OpenCalendar(milui) }
        menikur.setOnClickListener { OpenCalendar(menikur) }
        tikun.setOnClickListener { OpenCalendar(tikun) }
        hasara.setOnClickListener { OpenCalendar(hasara) }
        tzior.setOnClickListener { OpenCalendar(tzior) }
        cancel_turn.setOnClickListener { Cancel(closeTurn) }
        change_turn.setOnClickListener { change(closeTurn) }
        bnia_hadash.setOnClickListener {
            OpenCalendar(bnia_hadash)
        }
    }

    private fun change(closeTurn: Turn?) {
        val gson = Gson()
        val json = gson.toJson(user)
        Log.d("JSON", json)

        val jsonOfButton = gson.toJson(
            closeTurn?.type?.split("-")?.get(0)
                ?.substring(1, closeTurn.type.split("-")[0].length - 1)
        )
        val jsonOfCloseTurn = gson.toJson(closeTurn)
        val intent = Intent(this, CalendarActivity::class.java)
        intent.putExtra("change", "1")
        intent.putExtra("turnToChange", jsonOfCloseTurn)
        intent.putExtra("user", json)
        intent.putExtra("button", jsonOfButton)
        startActivity(intent)
        finish()
    }

    fun Cancel(closeTurn: Turn?) {
        listOfWaitList = dataManager.getListOfWaitList()
        for (i in 0..<listOfWaitList.size) {
            if (listOfWaitList[i].day == closeTurn!!.day && listOfWaitList[i].month == closeTurn.month && listOfWaitList[i].year == closeTurn.year) {
                listOfWaitList[i].waitList[closeTurn.indexInWaitList]--
                if (listOfWaitList[i].listOfTurnWaiting.isNotEmpty()) {
                    for (j in 0..<listOfWaitList[i].listOfTurnWaiting.size) {
                        if (listOfWaitList[i].listOfTurnWaiting[j].day == closeTurn.day && listOfWaitList[i].listOfTurnWaiting[j].month == closeTurn.month && listOfWaitList[i].listOfTurnWaiting[j].year == closeTurn.year && listOfWaitList[i].listOfTurnWaiting[j].hours == closeTurn.hours && listOfWaitList[i].listOfTurnWaiting[j].minutes == closeTurn.minutes) {
                            listOfTurns.add(listOfWaitList[i].listOfTurnWaiting[j])
                            listOfWaitList[i].listOfTurnWaiting.removeAt(j)
                        }
                    }
                    val notificationData = mapOf(
                        "title" to "Mikeyboo",
                        "body" to "Notification Body"
                    )

                    val message = user?.let {
                        RemoteMessage.Builder(it.token)
                            .setData(notificationData) // Use setData() instead of setNotification()
                            .build()
                    }

                    if (message != null) {
                        FirebaseMessaging.getInstance().send(message)
                    }
                }
                    /*
                    var notification = NotificationCompat.Builder(this,"Channel")
                    notification.setSmallIcon(R.drawable.baseline_access_time_24)
                        .setContentTitle("MikeyBoo")
                        .setContentText("התור לו המתנת התפנה ונקבע , על מנת לבטל/לצפות נא ללחוץ על ההודעה")
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)


                    var intent = Intent(this, OptionsActivty::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                    var pendingIntent: PendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_MUTABLE)
                    notification.setContentIntent(pendingIntent)
                    var manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                    if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        var channel = manager.getNotificationChannel("Channel")
                        if(channel == null) {
                            channel = NotificationChannel("Channel","name",NotificationManager.IMPORTANCE_HIGH)
                        }
                        channel.lightColor = R.color.pink
                        channel.enableVibration(true)
                        manager.createNotificationChannel(channel)
                    }
                    manager.notify(0,notification.build())

                   */
                viewModel.setWaitListTurns(listOfWaitList)
                waitListRef.setValue(listOfWaitList)
                break
                }
            }

        listOfTurns = dataManager.getListOfTurns()
        for (i in listOfTurns) {
            if (i.day == closeTurn!!.day && i.month == closeTurn.month && i.year == closeTurn.year && i.hours == closeTurn.hours && i.minutes == closeTurn.minutes) {
                listOfTurns.remove(i)
                dataManager.setListOfTurns(listOfTurns)
                viewModel.setListOfTurns(listOfTurns)
                turnsRef.setValue(listOfTurns)
                val builder = AlertDialog.Builder(this)
                builder.setTitle("ביטול תור!")
                builder.setMessage("התור בוטל בהצלחה")
                builder.setPositiveButton("אישור") { dialog, which ->
                    moveToOptions()
                }
                val dialog = builder.create()
                dialog.show()

            }
        }
    }

    private fun moveToOptions()
    {
        val intent = Intent(this, OptionsActivty::class.java)
        val gson = Gson()
        val json = gson.toJson(user)
        intent.putExtra("user from calendar", json)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.options_activty, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_options_activty)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun OpenCalendar(button: MaterialButton) {
        val gson = Gson()
        val json = gson.toJson(user)
        Log.d("JSON", json)
        val jsonOfButton = gson.toJson(button.text.split("-")[0].substring(0,button.text.split("-")[0].length-1))
        val intent = Intent(this, CalendarActivity::class.java)
        intent.putExtra("user", json)
        intent.putExtra("button", jsonOfButton)
        startActivity(intent)
    }

    private fun MoveToWaze() {
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
    }private fun loadDat() {
        turnsRef.addValueEventListener(object : ValueEventListener { // For realtime data fetching from DB
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value: ArrayList<Turn>? = dataSnapshot.getValue(object : GenericTypeIndicator<ArrayList<Turn>>() {})
                //Log.d("Turns in DataManager", value?.size.toString())
                var listOfUser: ArrayList<Turn>? = ArrayList()
                if (value != null) {
                    listOfTurns = value

                    Log.d("Turns list in DataManager", listOfTurns.size.toString())
                    for (i in listOfTurns) {
                        if (i.user?.userName == user!!.userName) {
                            listOfUser?.add(i)
                            if (listOfUser != null) {
                                dataManager.setListOfUsersTurns(listOfUser)
                            }
                        }
                    }
                }
                Log.d("Turns usersssssss in DataManager", listOfUser?.size.toString())
                dataManager.setListOfTurns(listOfTurns)
                if (listOfUser != null) {
                    dataManager.setListOfUsersTurns(listOfUser)
                }
                listOfUser?.sortWith(compareBy<Turn> { it.year }.thenBy { it.month }.thenBy { it.day }.thenBy { it.hours }.thenBy { it.minutes })

                listOfUser = listOfUser?.let { dataManager.getFutureListOfUsersTurns(it) }
                if(listOfUser?.isNotEmpty() == true) {
                    cancel_turn.visibility = View.VISIBLE
                    change_turn.visibility = View.VISIBLE
                    closeTurn = listOfUser[0]
                    if (closeTurn?.minutes == 0)
                        main_LST_scores.text =
                            closeTurn?.day.toString() + "/" + closeTurn?.month.toString() + "/" + closeTurn?.year.toString() + "      " + closeTurn?.hours.toString() + ":" + closeTurn?.minutes.toString() + "0" + "      " + closeTurn?.type?.substring(
                                1,
                                closeTurn?.type?.length!! - 1
                            )
                    else
                        main_LST_scores.text =
                            closeTurn?.day.toString() + "/" + closeTurn?.month.toString() + "/" + closeTurn?.year.toString() + "      " + closeTurn?.hours.toString() + ":" + closeTurn?.minutes.toString()  + "      " + closeTurn?.type?.substring(
                                1,
                                closeTurn?.type?.length!! - 1
                            )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
    }

    fun getFutureListOfUsersTurns(list: ArrayList<Turn>) : ArrayList<Turn> {
        val listOfFutureTurns = ArrayList<Turn>()
        val currentDate = LocalDate.now()
        val currentTime = LocalTime.now()
        for (turn in list) {
            if (currentDate.year <= turn.year && currentDate.monthValue == turn.month) {
                if (currentDate.dayOfMonth == turn.day) {
                    if (currentTime.hour == turn.hours) {
                        if (currentTime.minute < turn.minutes) {
                            listOfFutureTurns.add(turn)
                        }
                    } else if (currentTime.hour < turn.hours)
                        listOfFutureTurns.add(turn)
                } else if (currentDate.dayOfMonth < turn.day)
                    listOfFutureTurns.add(turn)
            }
            if(currentDate.year <= turn.year && currentDate.monthValue < turn.month)
                listOfFutureTurns.add(turn)
            if(currentDate.year < turn.year)
                listOfFutureTurns.add(turn)
        }
        listOfFutureTurns.sortWith(compareBy({it.day},{it.month},{it.year},{it.hours},{it.minutes}))
        return listOfFutureTurns
    }
}