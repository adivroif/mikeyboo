package com.example.mikeyboo.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mikeyboo.R
import com.example.mikeyboo.adapter.TurnsAdapter
import com.example.mikeyboo.databinding.ActivityBusinessSideBinding
import com.example.mikeyboo.firebaseConnections.firebaseConnections
import com.example.mikeyboo.models.Turn
import com.example.mikeyboo.models.Type
import com.example.mikeyboo.models.User
import com.example.mikeyboo.sharedData.DataManager
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BusinessSideActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityBusinessSideBinding
    private var list_of_users : ArrayList<User>? = ArrayList()
    private var list_of_types : ArrayList<Type>? = ArrayList()
    private var dataManager = DataManager()
    private lateinit var database : FirebaseDatabase
    private lateinit var turnsToday: MaterialTextView
    private var firebaseConnections = firebaseConnections()
    private var turnsRef= firebaseConnections.initDBConnection("turns")

    private lateinit var calendarView_in_business: CalendarView
    private lateinit var main_LST_scores_in_business: RecyclerView

    //For Date:
    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0
    private var listOfTurns: ArrayList<Turn>? = ArrayList()

    private var adapter: TurnsAdapter = TurnsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessSideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarBusinessSide.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayoutBusiness
        val navView: NavigationView = binding.navViewBusiness
        val navController = findNavController(R.id.nav_host_fragment_content_business_side)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home_business,R.id.nav_hours_business,R.id.nav_customer_list_business,R.id.nav_turns_type_business
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        findViews()
        dataManager.updateWorkHoursFromDB()
        dataManager.updateUsersFromDB()
        dataManager.updatePhotosFromDB()
        Log.d("Users in business", list_of_users?.size.toString())
        list_of_types = dataManager.getListOfTypes()
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd MM yyyy")
        val formattedDate = currentDate.format(formatter)
        day = formattedDate.split(" ")[0].toInt()
        month = formattedDate.split(" ")[1].toInt()
        year = formattedDate.split(" ")[2].toInt()
        loadDat()
        initViews()
    }


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private fun initDBConnection(path: String): DatabaseReference {
        database = Firebase.database
        return database.getReference(path)
    }

    private fun loadDat() {
        turnsRef.addValueEventListener(object : ValueEventListener { // For realtime data fetching from DB
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                /*
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value: ArrayList<Turn>? = dataSnapshot.getValue(object : GenericTypeIndicator<ArrayList<Turn>>() {})
                Log.d("Turns in DataManager", value?.size.toString())
                var list = value
                if (value != null) {
                    listOfTurns=value
                }

                 */
                val value: ArrayList<Turn>? =
                    dataSnapshot.getValue(object : GenericTypeIndicator<ArrayList<Turn>>() {})
                //Log.d("Turns in DataManager", value?.size.toString())
                if (value != null) {
                    listOfTurns = value
                }
                Log.d("Turns list in DataManager", listOfTurns?.size.toString())
                listOfTurns?.sortWith(compareBy<Turn> { it.year }.thenBy { it.month }
                    .thenBy { it.day }.thenBy { it.hours }.thenBy { it.minutes })
                dataManager.setListOfTurns(listOfTurns!!)
                adapter = TurnsAdapter()
                adapter.turnsAdapter(this@BusinessSideActivity, listOfTurns)
                dataManager.setListOfTurns(listOfTurns!!)
                openTurnsOfThisDay(year, month, day)

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
    }


    private fun findViews() {
        calendarView_in_business = findViewById(R.id.calendarView_in_business)
        main_LST_scores_in_business = findViewById(R.id.main_LST_scores_in_business)
        turnsToday = findViewById(R.id.turnsToday)
    }

    private fun initViews() {
            calendarView_in_business.setOnDateChangeListener { view, year, month, dayOfMonth ->
                openTurnsOfThisDay(
                    year,
                    month + 1,
                    dayOfMonth
                )
                // Do something with the selected date
            }
        }

    private fun openTurnsOfThisDay(year: Int, month: Int, dayOfMonth: Int) {
        listOfTurns = dataManager.getListOfTurnsForDay(year, month, dayOfMonth)
        if (listOfTurns?.isEmpty() == true) {
            if (turnsToday != null)
                turnsToday.visibility = View.VISIBLE
            if (main_LST_scores_in_business != null)
                main_LST_scores_in_business.visibility = View.GONE
        } else {
            if (turnsToday != null) {
                turnsToday.setVisibility(View.GONE);
            }
            if (main_LST_scores_in_business != null) {
                main_LST_scores_in_business.setVisibility(View.VISIBLE);
            }
        }
        adapter = TurnsAdapter()
        adapter.turnsAdapter(this, listOfTurns)

        var linearManager = LinearLayoutManager(this)
        linearManager.setOrientation(LinearLayoutManager.VERTICAL)
        main_LST_scores_in_business.setLayoutManager(linearManager)
        main_LST_scores_in_business.setAdapter(adapter)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.business_side, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_business_side)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}