package com.example.mikeyboo.firebaseConnections

import android.util.Log
import com.example.mikeyboo.models.Turn
import com.example.mikeyboo.models.Type
import com.example.mikeyboo.models.User
import com.example.mikeyboo.models.WorkDayHour
import com.example.mikeyboo.models.storageSave
import com.example.mikeyboo.models.waitListsDB
import com.example.mikeyboo.sharedData.DataManager.Companion.listOfHours
import com.example.mikeyboo.sharedData.DataManager.Companion.listOfMinutes
import com.example.mikeyboo.sharedData.DataManager.Companion.listOfReviews
import com.example.mikeyboo.sharedData.DataManager.Companion.listOfStoragePhotos
import com.example.mikeyboo.sharedData.DataManager.Companion.listOfTurns
import com.example.mikeyboo.sharedData.DataManager.Companion.listOfTurnsFrom
import com.example.mikeyboo.sharedData.DataManager.Companion.listOfTypes
import com.example.mikeyboo.sharedData.DataManager.Companion.listOfUsers
import com.example.mikeyboo.sharedData.DataManager.Companion.listOfWaitList
import com.example.mikeyboo.sharedData.DataManager.Companion.listOfWorkHours
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class firebaseConnections {
    private lateinit var database : FirebaseDatabase
    private var userRef  = initDBConnection("user")
    private var turnsRef = initDBConnection("turns")
    private var typesRef = initDBConnection("type")
    private var waitListRef = initDBConnection("waitList")
    private var workHoursRef = initDBConnection("workHours")
    private var reviewRef = initDBConnection("reviews")
    private var storageRef = initDBConnection("storage")

    fun initDBConnection(path: String) : DatabaseReference {
        database = Firebase.database
        return database.getReference(path)
    }

    fun updateTypes() : ArrayList<Type>
    {
        var listOfType = ArrayList<Type>()
        typesRef.addValueEventListener(object : ValueEventListener { // For realtime data fetching from DB
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value: ArrayList<Type>? = dataSnapshot.getValue(object : GenericTypeIndicator<ArrayList<Type>>() {})
                Log.d("Types in DataManager", value?.size.toString())
                if (value != null) {
                    listOfType = value
                }
            }



            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
        return listOfType
    }

    fun updatePhotos() : ArrayList<storageSave>
    {
        var listOfStoragePhoto = ArrayList<storageSave>()
        storageRef.addValueEventListener(object : ValueEventListener { // For realtime data fetching from DB
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value: ArrayList<storageSave>? = dataSnapshot.getValue(object : GenericTypeIndicator<ArrayList<storageSave>>() {})
                Log.d("Types in DataManager", value?.size.toString())
                if (value != null) {
                    listOfStoragePhoto = value
                }
            }



            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
        return listOfStoragePhoto
    }

    fun updateReviews() : ArrayList<String>
    {
        var listOfReview = ArrayList<String>()
        reviewRef.addValueEventListener(object : ValueEventListener { // For realtime data fetching from DB
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value: ArrayList<String>? = dataSnapshot.getValue(object : GenericTypeIndicator<ArrayList<String>>() {})
                Log.d("Reviews in DataManager", value?.size.toString())
                if (value != null) {
                    listOfReview = value
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
        return listOfReview
    }

    fun updateWaitList() : ArrayList<waitListsDB>
    {
        var listOfWait = ArrayList<waitListsDB>()
        waitListRef.addValueEventListener(object : ValueEventListener { // For realtime data fetching from DB
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value: ArrayList<waitListsDB>? = dataSnapshot.getValue(object : GenericTypeIndicator<ArrayList<waitListsDB>>() {})
                if (value != null) {
                    listOfWait = value
                }
            }



            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
        return listOfWait
    }

    fun updateUsers() : ArrayList<User>
    {
        var listOfUser = ArrayList<User>()
        userRef.addValueEventListener(object : ValueEventListener { // For realtime data fetching from DB
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value: ArrayList<User>? = dataSnapshot.getValue(object : GenericTypeIndicator<ArrayList<User>>() {})
                Log.d("Users in DataManager", value?.size.toString())
                if (value != null) {
                    listOfUser = value
                }
                Log.d("Userssssssssssss in DataManager", listOfUser.toString())
            }



            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
        Log.d("Userssssssssssss22222 in DataManager", listOfUser.toString())
        return listOfUser

    }

    fun updateTurns(user: User) : ArrayList<Turn> {
        val listOfTurn = ArrayList<Turn>()
        var listOfTurnFrom = ArrayList<Turn>()
        turnsRef.addValueEventListener(object : ValueEventListener { // For realtime data fetching from DB
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value: ArrayList<Turn>? = dataSnapshot.getValue(object : GenericTypeIndicator<ArrayList<Turn>>() {})
                //Log.d("Turns in DataManager", value?.size.toString())
                if (value != null) {
                    listOfTurnFrom = value
                }
                //Log.d("Turns in DataManager1", listOfTurnsFrom.size.toString())
                for (i in 0..<listOfTurnFrom.size) {
                    if(user.userName == "michaellevi")
                        listOfTurn.add(listOfTurnFrom[i])
                    if (user.userName == listOfTurnFrom[i].user?.userName && user.password == listOfTurnFrom[i].user?.password) {
                        listOfTurn.add(listOfTurnFrom[i])
                    }
                }
                //Log.d("Turns in DataManager2", listOfTurns.size.toString())
            }



            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
        return listOfTurn
    }
}