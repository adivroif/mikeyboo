package com.example.mikeyboo

class listOfUsers() {

    init {
        if(listOfUsers.isNullOrEmpty())
            listOfUsers = ArrayList<User>()
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