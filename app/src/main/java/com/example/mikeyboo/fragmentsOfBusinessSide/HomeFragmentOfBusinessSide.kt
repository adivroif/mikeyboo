package com.example.mikeyboo.fragmentsOfBusinessSide

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mikeyboo.R
import com.example.mikeyboo.adapter.TurnsAdapter
import com.example.mikeyboo.databinding.ActivityBusinessSideBinding
import com.example.mikeyboo.databinding.FragmentHomeBusinessSideBinding
import com.example.mikeyboo.fragments_of_customer_side.HomeViewModel
import com.example.mikeyboo.models.Turn
import com.example.mikeyboo.models.Type
import com.example.mikeyboo.models.User
import com.example.mikeyboo.sharedData.DataManager
import com.example.mikeyboo.sharedData.SharedViewModel
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

class HomeFragmentOfBusinessSide : Fragment() {
    private lateinit var binding: FragmentHomeBusinessSideBinding
    private var list_of_users: ArrayList<User>? = ArrayList<User>()
    private var list_of_types: ArrayList<Type>? = ArrayList<Type>()
    private var dataManager = DataManager()

    //For Date:
    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0
    private var listOfTurns: ArrayList<Turn>? = ArrayList<Turn>()
    private lateinit var database: FirebaseDatabase
    private var turnsRef=initDBConnection("turns")
    private var adapter: TurnsAdapter = TurnsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModelOfBusinessSide::class.java)
        // Inflate the layout for this fragment
        binding = FragmentHomeBusinessSideBinding.inflate(inflater, container, false)
        dataManager.updateWorkHoursFromDB()
        dataManager.updateUsersFromDB()
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
        return binding.root
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
                val value: ArrayList<Turn>? = dataSnapshot.getValue(object : GenericTypeIndicator<ArrayList<Turn>>() {})
                //Log.d("Turns in DataManager", value?.size.toString())
                if (value != null) {
                    listOfTurns = value
                }
                Log.d("Turns list in DataManager", listOfTurns?.size.toString())
                listOfTurns?.let { dataManager.setListOfTurns(it) }
                listOfTurns?.sortWith(compareBy<Turn> { it.year }.thenBy { it.month }.thenBy { it.day }.thenBy { it.hours }.thenBy { it.minutes })
                adapter = TurnsAdapter()
                adapter.turnsAdapter(requireContext(), listOfTurns)
                openTurnsOfThisDay(year, month, day)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
    }


    private fun openTurnsOfThisDay(year: Int, month: Int, dayOfMonth: Int) {
        listOfTurns = dataManager.getListOfTurnsForDay(year, month, dayOfMonth)
        if (listOfTurns?.isEmpty() == true) {
            if (binding.turnsToday != null)
                binding.turnsToday.visibility = View.VISIBLE
            if (binding.mainLSTScoresInBusiness != null)
                binding.mainLSTScoresInBusiness.visibility = View.GONE
        } else {
            if (binding.turnsToday != null) {
                binding.turnsToday.setVisibility(View.GONE);
            }
            if (binding.mainLSTScoresInBusiness != null) {
                binding.mainLSTScoresInBusiness.setVisibility(View.VISIBLE);
            }
            adapter = TurnsAdapter()
            adapter.turnsAdapter(requireContext(), listOfTurns)

            var linearManager = LinearLayoutManager(requireContext())
            linearManager.setOrientation(LinearLayoutManager.VERTICAL)
            binding.mainLSTScoresInBusiness.setLayoutManager(linearManager)
            binding.mainLSTScoresInBusiness.setAdapter(adapter)
        }
    }

    private fun initViews() {
            binding.calendarViewInBusiness.setOnDateChangeListener { view, year, month, dayOfMonth ->
                openTurnsOfThisDay(
                    year,
                    month + 1,
                    dayOfMonth
                )
                // Do something with the selected date
            }
        }
    }