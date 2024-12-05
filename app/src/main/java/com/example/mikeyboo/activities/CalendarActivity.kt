package com.example.mikeyboo.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mikeyboo.R
import com.example.mikeyboo.databinding.ActivityCalendarBinding
import com.example.mikeyboo.firebaseConnections.firebaseConnections
import com.example.mikeyboo.models.Turn
import com.example.mikeyboo.models.User
import com.example.mikeyboo.models.WorkDayHour
import com.example.mikeyboo.models.savePhoto
import com.example.mikeyboo.models.storageSave
import com.example.mikeyboo.models.waitListsDB
import com.example.mikeyboo.sharedData.DataManager
import com.example.mikeyboo.sharedData.SharedViewModel
import com.google.android.material.button.MaterialButton
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import com.google.gson.Gson
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date


class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarBinding
    private lateinit var calendarView: CalendarView

    //For Date:
    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0

    //For Turns and Buttons:
    private var listOfTurns: ArrayList<Turn>? = ArrayList<Turn>()
    private val listOfTurnsDecideByManager = ArrayList<Turn>()
    private val listOfButtonsDecideByManager = ArrayList<MaterialButton>()

    private var listOfWaitList = ArrayList<waitListsDB>()
    private var waitList = MutableList<Int>(6) { -1 }

    private val date = Date()

    private lateinit var storage: FirebaseStorage

    private lateinit var storageRef: StorageReference

    private lateinit var photoRef: StorageReference

    private var storageRef2 = initDBConnection("storage")

    private lateinit var imagesRef: StorageReference

    var uri : Uri? = null

    private var firebaseConnections = firebaseConnections()

    private var turnsRef = firebaseConnections.initDBConnection("turns")

    private var waitListRef = firebaseConnections.initDBConnection("waitList")

    private lateinit var database: FirebaseDatabase

    private var user: User? = null

    private val viewModel: SharedViewModel by viewModels()

    private var turn = Turn(user, null.toString(), date.day, date.month, date.year, 0, 0,0)

    private lateinit var turnToChange:Turn

    private lateinit var listOfTurnsYours : ArrayList<Int>

    private var indexOfTurn = 0

    private var indexOfWaitList =-2

    private var getFromOptions: String? = null

    private val dataManager = DataManager()

    private var listOfStorageRef = dataManager.getListOfStoragePhotos()

    private var listOfWorkHours = ArrayList<WorkDayHour>()

    private var listOfType = dataManager.getListOfTypes()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        findViews()
        initViews()
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd MM yyyy")
        val formattedDate = currentDate.format(formatter)
        day = formattedDate.split(" ")[0].toInt()
        month = formattedDate.split(" ")[1].toInt()
        year = formattedDate.split(" ")[2].toInt()
        turn.day = day
        turn.month = month
        turn.year = year
        listOfButtonsDecideByManager.add(binding.turn900)
        listOfButtonsDecideByManager.add(binding.turn1030)
        listOfButtonsDecideByManager.add(binding.turn1200)
        listOfButtonsDecideByManager.add(binding.turn1330)
        listOfButtonsDecideByManager.add(binding.turn1500)
        listOfButtonsDecideByManager.add(binding.turn1630)

        listOfTurnsYours = MutableList(listOfButtonsDecideByManager.size) { 0 } as ArrayList<Int>

        listOfWaitList = dataManager.getListOfWaitList()
        listOfWorkHours = dataManager.getListOfWorkHours()
        listOfTurns = dataManager.getListOfTurns()

        storage = Firebase.storage
        this.loadDataWaitList(day, month, year)
        val getFromLogin: String = intent.getStringExtra("user")!!
        user = User(
            getFromLogin.split(",")[1].split(":")[1],
            getFromLogin.split(",")[0].split(":")[1],
            getFromLogin.split(",")[3].split(":")[1],
            getFromLogin.split(",")[5].split(":")[1],
            getFromLogin.split(",")[2].split(":")[1],
            getFromLogin.split(",")[4].split(":")[1]
        )
        user = dataManager.getUser()

        val button: String = intent.getStringExtra("button").toString()
        turn.type = button
        turn.user = this.user!!

        listOfTurnsDecideByManager.add(
            Turn(
                user!!,
                button,
                day,
                month,
                year,
                listOfWorkHours[0].startHour,
                0,0
            )
        )
        if (listOfTurnsDecideByManager[0].minutes == 0)
            listOfButtonsDecideByManager[0].text =
                "שעה : " + listOfTurnsDecideByManager[0].hours.toString() + ":" + listOfTurnsDecideByManager[0].minutes.toString() + "0"
        else
            listOfButtonsDecideByManager[0].text =
                "שעה : " + listOfTurnsDecideByManager[0].hours.toString() + ":" + listOfTurnsDecideByManager[0].minutes.toString()

        for (i in 1..<listOfWorkHours.size) {
            if (listOfTurnsDecideByManager[i - 1].minutes == 30) {
                listOfTurnsDecideByManager.add(
                    Turn(
                        user!!,
                        button,
                        day,
                        month,
                        year,
                        (listOfWorkHours[i].startHour + i * 1.5).toInt(),
                        0,i
                    )
                )
            } else
                listOfTurnsDecideByManager.add(
                    Turn(
                        user!!,
                        button,
                        day,
                        month,
                        year,
                        (listOfWorkHours[i].startHour + i * 1.5).toInt(),
                        30,i
                    )
                )

            if (listOfTurnsDecideByManager[i].minutes == 0)
                listOfButtonsDecideByManager[i].text =
                    "שעה : " + listOfTurnsDecideByManager[i].hours.toString() + ":" + listOfTurnsDecideByManager[i].minutes.toString() + "0"
            else
                listOfButtonsDecideByManager[i].text =
                    "שעה : " + listOfTurnsDecideByManager[i].hours.toString() + ":" + listOfTurnsDecideByManager[i].minutes.toString()

        }
        openTurnsAvailableOfThisDay(year,month,day)
        getFromOptions = intent.getStringExtra("change")
        val getTurnFromOptions : String? = intent.getStringExtra("turnToChange")
        if(getTurnFromOptions!=null) {
            turnToChange = Gson().fromJson(getTurnFromOptions, Turn::class.java)
            turnToChange.type = turnToChange.type.substring(1, turnToChange.type.length - 1)
        }

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            binding.addedImage.setImageURI(it)
            if(it != null) {
                uri = it
            }
        }
        binding.btnChoosePhoto.setOnClickListener{
            pickImage.launch("image/*")
        }

        storageRef = Firebase.storage.reference
        imagesRef = storageRef.child("images")


    }

    private fun initViews() {
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            setVisibleToTurns(dayOfMonth, month + 1, year);openTurnsAvailableOfThisDay(
            year,
            month + 1,
            dayOfMonth
        )
            // Do something with the selected date
        }
        binding.chooseDate.setOnClickListener { v: View -> keepTurn() }
        binding.chooseDate.visibility = View.INVISIBLE
        binding.turn900.setOnClickListener { v: View ->
            changeColor(0);invisible(
            listOfTurnsDecideByManager[0].hours,0
        );setTurn(listOfTurnsDecideByManager[0]);setIndex(0)
        }
        binding.turn1030.setOnClickListener { v: View ->
            changeColor(1); invisible(
            listOfTurnsDecideByManager[1].hours,1
        );setTurn(listOfTurnsDecideByManager[1]);setIndex(1)
        }
        binding.turn1200.setOnClickListener { v: View ->
            changeColor(2); invisible(
            listOfTurnsDecideByManager[2].hours,2
        );setTurn(listOfTurnsDecideByManager[2]);setIndex(2)
        }
        binding.turn1330.setOnClickListener { v: View ->
            changeColor(3); invisible(
            listOfTurnsDecideByManager[3].hours,3
        );setTurn(listOfTurnsDecideByManager[3]);setIndex(3)
        }
        binding.turn1500.setOnClickListener { v: View ->
            changeColor(4); invisible(
            listOfTurnsDecideByManager[4].hours,4
        );setTurn(listOfTurnsDecideByManager[4]);setIndex(4)
        }
        binding.turn1630.setOnClickListener { v: View ->
            changeColor(5); invisible(
            listOfTurnsDecideByManager[5].hours,5
        );setTurn(listOfTurnsDecideByManager[5]);setIndex(5)
        }
        binding.ReturnToMain.setOnClickListener { v: View -> returnToMain() }
    }

    private fun returnToMain() {
        val gson = Gson()
        val json = gson.toJson(user)
        var intent = Intent(this, OptionsActivty::class.java)
        intent.putExtra("user from calendar", json)
        startActivity(intent)
    }


    private fun findViews() {
        calendarView = findViewById(R.id.calendarView)
        /*
        turn900 = findViewById(R.id.turn900)
        turn1030 = findViewById(R.id.turn1030)
        turn1200 = findViewById(R.id.turn1200)
        turn1330 = findViewById(R.id.turn1330)
        turn1500 = findViewById(R.id.turn1500)
        turn1630 = findViewById(R.id.turn1630)
        chooseDate = findViewById(R.id.chooseDate)
    */
    }

    private fun setIndex(i: Int) {
        indexOfTurn = i
    }

    private fun loadDataWaitList(dayOfMonth: Int, month: Int, year: Int) {
        indexOfWaitList = -2
        if (listOfWaitList.isNotEmpty()) {
            for (i in 0..<listOfWaitList.size) {
                Log.d(
                    "value",
                    listOfWaitList[i].day.toString() + " = " + dayOfMonth.toString() + " , " + listOfWaitList[i].month.toString() + " = " + month.toString() + " , " + listOfWaitList[i].year.toString() + " = " + year.toString()
                )
                if (listOfWaitList[i].day == dayOfMonth && listOfWaitList[i].month == month && listOfWaitList[i].year == year) {
                    Log.d("Im in", "innnnnnnnnnnnnnnnnn")
                    waitList = listOfWaitList[i].waitList
                    indexOfWaitList = i
                    //viewModel.setIndexListOfWaitList(value[i].waitList,i)
                    //TODO(add to waitList in this class as the parameters in the declaration of the function and also in sharedViewModel,update wait list of this class)
                }
            }
            if (indexOfWaitList == -2 || indexOfWaitList == -1) {
                waitList = MutableList(6) { -1 }
                indexOfWaitList = -1
            }

            viewModel.setWaitList(waitList)
        }
        Log.d("IndexOfWaitList", indexOfWaitList.toString())
        //invisibleTurns()
    }


    private fun initDBConnection(path: String): DatabaseReference {
        database = Firebase.database
        return database.getReference(path)
    }

    private fun setListOfWaitListFromDB(waitList: ArrayList<waitListsDB>) {
        this.listOfWaitList = waitList
    }

    private fun changeColor(postion: Int) {
        if(listOfTurnsYours[postion] == 0) {
            if (waitList[postion] > -1)
                binding.chooseDate.text = "הירשם לרשימת המתנה"
            else
                binding.chooseDate.text = "בחר"
            binding.chooseDate.visibility = View.VISIBLE
            listOfButtonsDecideByManager[postion].setBackgroundColor(resources.getColor(R.color.black))
            listOfButtonsDecideByManager[postion].setTextColor(resources.getColor(R.color.pink))
        }
        else {
            binding.chooseDate.visibility = View.INVISIBLE
            listOfButtonsDecideByManager[postion].setTextColor(resources.getColor(R.color.black))
        }
    }

    private fun invisible(hours: Int,index:Int) {
        for (i in 0..<listOfTurnsDecideByManager.size) {
            if (listOfTurnsDecideByManager[i].hours != hours && listOfTurnsYours[i] == 0)
                changeColorToWhite(listOfButtonsDecideByManager[i])
            else if (listOfTurnsYours[i] == 1) {
                listOfButtonsDecideByManager[i].setBackgroundColor(resources.getColor(R.color.green))
                listOfButtonsDecideByManager[i].setTextColor(resources.getColor(R.color.black))
            }
        }
    }

    private fun changeColorToWhite(button: MaterialButton) {
        button.setBackgroundColor(resources.getColor(R.color.white))
        button.setTextColor(resources.getColor(R.color.pink))
    }

    private fun setVisibleToTurns(dayOfMonth: Int, month: Int, year: Int) {
        this.day = dayOfMonth
        this.month = month
        this.year = year
    }

    @SuppressLint("SuspiciousIndentation")
    private fun keepTurn() {
        var change = 0
        if(this.getFromOptions != null) {
            for (i in 0..<listOfTurns!!.size) {
                if (listOfTurns!![i].day == turnToChange.day && listOfTurns!![i].month == turnToChange.month && listOfTurns!![i].year == turnToChange.year && listOfTurns!![i].hours == turnToChange.hours && listOfTurns!![i].minutes == turnToChange.minutes) {
                    listOfTurns!![i] = turn
                    break
                }
            }
            for (i in 0..<listOfWaitList.size) {
                if (listOfWaitList[i].day == turnToChange.day && listOfWaitList[i].month == turnToChange.month && listOfWaitList[i].year == turnToChange.year) {
                    listOfWaitList[i].waitList[turnToChange.indexInWaitList]--
                    if(listOfWaitList[i].listOfTurnWaiting.isNotEmpty()) {
                        for (j in 0..<listOfWaitList[i].listOfTurnWaiting.size) {
                            if (listOfWaitList[i].listOfTurnWaiting[j].day == turnToChange.day && listOfWaitList[i].listOfTurnWaiting[j].month == turnToChange.month && listOfWaitList[i].listOfTurnWaiting[j].year == turnToChange.year && listOfWaitList[i].listOfTurnWaiting[j].hours == turnToChange.hours && listOfWaitList[i].listOfTurnWaiting[j].minutes == turnToChange.minutes) {
                                listOfTurns!!.add(listOfWaitList[i].listOfTurnWaiting[j])
                                listOfWaitList[i].listOfTurnWaiting.removeAt(j)
                            }
                        }
                    }
                }
                if (listOfWaitList[i].day == turn.day && listOfWaitList[i].month == turn.month && listOfWaitList[i].year == turn.year) {
                    listOfWaitList[i].waitList[turn.indexInWaitList]++
                    change = 1
                }
            }
            if(change == 0) {
                listOfWaitList.add(waitListsDB(waitList, day, month, year, turn.type, ArrayList()))
                listOfWaitList[listOfWaitList.size - 1].waitList[turn.indexInWaitList]++
            }
            val builder = AlertDialog.Builder(this)
            builder.setTitle("שינוי תור!")
            builder.setMessage("התור שונה בהצלחה")
            builder.setPositiveButton("אישור") { dialog, which ->
                moveToOptions()
            }
            val dialog = builder.create()
            dialog.show()
        }
        else {
            if (indexOfWaitList == -1) {
                listOfTurns?.add(turn)
                waitList[indexOfTurn]++
                listOfWaitList.add(
                    waitListsDB(
                        waitList,
                        day,
                        month,
                        year,
                        turn.type.split("-")[0],
                        ArrayList()
                    )
                )
                val builder = AlertDialog.Builder(this)
                builder.setTitle("תור נקבע!")
                builder.setMessage("הפעולה בוצעה בהצלחה.")
                builder.setPositiveButton("אישור") { dialog, which ->
                    moveToOptions()
                }
                val dialog = builder.create()
                dialog.show()
            } else if (waitList[indexOfTurn] == -1) {
                listOfTurns?.add(turn)
                waitList[indexOfTurn]++
                listOfWaitList[indexOfWaitList].waitList = waitList
                 val builder = AlertDialog.Builder(this)
                    builder.setTitle("תור נקבע!")
                    builder.setMessage("הפעולה בוצעה בהצלחה.")
                    builder.setPositiveButton("אישור") { dialog, which ->
                        moveToOptions()

                    }
                    val dialog = builder.create()
                    dialog.show()
            } else {
                waitList[indexOfTurn]++
                listOfWaitList[indexOfWaitList].waitList = waitList
                listOfWaitList[indexOfWaitList].listOfTurnWaiting.add(turn)
                val builder = AlertDialog.Builder(this)
                builder.setTitle("מיקום ברשימת המתנה נקבע!")
                builder.setMessage("הפעולה בוצעה בהצלחה , מיקומך בתור הוא " + waitList[indexOfTurn])
                builder.setPositiveButton("אישור") { dialog, which ->
                    moveToOptions()
                }
                val dialog = builder.create()
                dialog.show()
            }
        }
            photoRef = storageRef.child(turn.user!!.userName + turn.day + turn.month + turn.year + turn.hours + turn.minutes)
    }

    private fun moveToOptions()
    {
        var photoUrl = ""
        uri?.let {
            photoRef.putFile(it)
                .addOnSuccessListener { task ->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { url ->
                            photoUrl = url.toString()
                            listOfStorageRef.add(storageSave(savePhoto(photoUrl,turn)))
                            storageRef2.setValue(listOfStorageRef)
                        }
                }
        }
        waitListRef.setValue(listOfWaitList)
        dataManager.setListOfStoragePhotos(listOfStorageRef)
        dataManager.updateTurnsFromDB(user!!)
        dataManager.setListOfTurns(listOfTurns!!)
        turnsRef.setValue(listOfTurns)
        Log.d("listOfTurns", listOfTurns?.size.toString())
        viewModel.setListOfWaitList(listOfWaitList)
        listOfTurns?.let { viewModel.setListOfTurns(it) }
        val gson = Gson()
        val json = gson.toJson(user)
        var intent = Intent(this, OptionsActivty::class.java)
        intent.putExtra("user from calendar", json)
        startActivity(intent)
        finish()
    }

    private fun setTurn(turn: Turn) {
        this.turn = turn
    }

    @SuppressLint("SetTextI18n")
    private fun openTurnsAvailableOfThisDay(year: Int, month: Int, dayOfMonth: Int) {
        loadDataWaitList(dayOfMonth, month, year)
        setVisibleToTurns(dayOfMonth, month, year)
        for (i in listOfButtonsDecideByManager)
            changeColorToWhite(i)
        initialTextOfTurns()
        binding.chooseDate.text = "בחר"
        this.day = dayOfMonth
        this.month = month
        this.year = year
        Log.d(
            "Day",
            dayOfMonth.toString() + " , " + month.toString() + " , " + year.toString()
        )
        var countOfTurns = 0
        //TODO(LOAD WAIT LIST FROM DATABASE AND UPDATE IT)
        if (listOfTurns?.isNotEmpty() == true) {
                    for (j in 0..<listOfTurnsDecideByManager.size) {
                        countOfTurns++
                        checkIfHisTurn(dayOfMonth, month,year,j, listOfTurnsDecideByManager[j])
                        if (waitList[j] > -1) {
                            listOfButtonsDecideByManager[j].setBackgroundColor(
                                resources.getColor(
                                    R.color.red
                                )
                            )
                            if (listOfTurnsDecideByManager[j].minutes == 0)
                                listOfButtonsDecideByManager[j].text =
                                    "שעה : " + listOfTurnsDecideByManager[j].hours + ":" + listOfTurnsDecideByManager[j].minutes + "0" + " - " + "לפנייך " + waitList[j].toString() + " בנות"
                            else
                                listOfButtonsDecideByManager[j].text =
                                    "שעה : " + listOfTurnsDecideByManager[j].hours + ":" + listOfTurnsDecideByManager[j].minutes + " - " + "לפנייך " + waitList[j].toString() + " בנות"
                        } else {

                            if (listOfTurnsDecideByManager[j].minutes == 0)
                                listOfButtonsDecideByManager[j].text =
                                    "שעה : " + listOfTurnsDecideByManager[j].hours.toString() + ":" + listOfTurnsDecideByManager[j].minutes.toString() + "0"
                            else
                                listOfButtonsDecideByManager[j].text =
                                    "שעה : " + listOfTurnsDecideByManager[j].hours.toString() + ":" + listOfTurnsDecideByManager[j].minutes.toString()
                        }

                        if (listOfTurnsYours[j] == 1) {
                            listOfButtonsDecideByManager[j].setBackgroundColor(resources.getColor(R.color.green))
                            listOfButtonsDecideByManager[j].setTextColor(resources.getColor(R.color.black))
                            if (listOfTurnsDecideByManager[j].minutes == 0)
                                listOfButtonsDecideByManager[j].text =
                                    "שעה : " + listOfTurnsDecideByManager[j].hours.toString() + ":" + listOfTurnsDecideByManager[j].minutes.toString() + "0, התור שלך."
                            else
                                listOfButtonsDecideByManager[j].text =
                                    "שעה : " + listOfTurnsDecideByManager[j].hours.toString() + ":" + listOfTurnsDecideByManager[j].minutes.toString() + ", התור שלך."
                        }
                    }

            if (countOfTurns == 0) {
                for (i in 0..<listOfButtonsDecideByManager.size) {
                    listOfButtonsDecideByManager[i].setBackgroundColor(resources.getColor(R.color.white))
                    listOfButtonsDecideByManager[i].text =
                        listOfTurnsDecideByManager[i].hours.toString() + ":" + listOfTurnsDecideByManager[i].minutes.toString()
                }
            }
        }
                for (i in listOfTurnsDecideByManager) {
                    i.day = dayOfMonth
                    i.month = month
                    i.year = year
                }
            this.day = dayOfMonth
            this.month = month
            this.year = year
            turn.day = dayOfMonth
            turn.month = month
            turn.year = year

    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }


    private fun checkIfHisTurn(dayOfMonth: Int, month: Int, year: Int,j: Int, turn: Turn) {
        val userList = dataManager.getListOfUsersTurns()
        for (i in 0..<userList.size) {
            if (userList[i].day == dayOfMonth && userList[i].month == month && userList[i].year == year && userList[i].hours == turn.hours && userList[i].minutes == turn.minutes) {
                listOfTurnsYours[j] = 1
                return
            }
        }
        listOfTurnsYours[j] = 0
    }

    private fun initialTextOfTurns() {
        for (j in 0..<listOfTurnsDecideByManager.size) {
            if (listOfTurnsDecideByManager[j].minutes == 0)
                listOfButtonsDecideByManager[j].text =
                    "שעה : " + listOfTurnsDecideByManager[j].hours.toString() + ":" + listOfTurnsDecideByManager[j].minutes.toString() + "0"
            else
                listOfButtonsDecideByManager[j].text =
                    "שעה : " + listOfTurnsDecideByManager[j].hours.toString() + ":" + listOfTurnsDecideByManager[j].minutes.toString()
        }
    }
}
