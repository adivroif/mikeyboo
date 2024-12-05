package com.example.mikeyboo.models

import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class listOfUsers() {

    private var userRef : DatabaseReference

    private lateinit var database : FirebaseDatabase

    init {
        userRef = initDBConnection("user")
        if(listOfUsers.isNullOrEmpty())
         listOfUsers = ArrayList<User>()
    }

    private fun initDBConnection(path: String) : DatabaseReference {
        database = Firebase.database
        return database.getReference(path)
    }

    fun getListOfUsers() : ArrayList<User>
    {
        return listOfUsers
    }

    fun addToList(player: User)
    {
        listOfUsers.add(player)
    }
    companion object
    {
        var listOfUsers: ArrayList<User> = ArrayList<User>()
    }
}