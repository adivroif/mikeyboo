package com.example.mikeyboo

class DataManager {
    private var listOfUsers : ArrayList<User>
    private var list : listOfUsers = listOfUsers()

    init {
        listOfUsers = listOfUsers().getListOfUsers()

        if(listOfUsers.isEmpty())
            listOfUsers = ArrayList<User>()
    }

    fun getList(): listOfUsers
    {
        return this.list
    }

    fun addUser(user: User){
        list.addToList(user)
        listOfUsers.sortBy { it.name }
    }

    fun getUser(userName: String,password:String) : User?
    {
        for (user : User in listOfUsers)
        {
            if(user.userName == userName && user.password == password)
                return user
        }
        return null
    }

    fun checkDataOfUser() : Boolean {
        return false
    }
}