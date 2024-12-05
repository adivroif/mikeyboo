package com.example.mikeyboo.models

class User constructor(
    var name : String,
    var mail : String,
    var phoneNumber : String,
    var userName : String,
    var password : String,
    var token:String
) {
    fun toArrayList(): ArrayList<User> {
        val list_of_users = ArrayList<User>()
        list_of_users.add(this)
        return list_of_users
    }

    constructor() : this("", "", "", "", "", "")

}