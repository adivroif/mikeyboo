package com.example.mikeyboo.sharedData

import android.util.Log
import com.example.mikeyboo.firebaseConnections.firebaseConnections
import com.example.mikeyboo.models.Turn
import com.example.mikeyboo.models.Type
import com.example.mikeyboo.models.User
import com.example.mikeyboo.models.WorkDayHour
import com.example.mikeyboo.models.storageSave
import com.example.mikeyboo.models.waitListsDB
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.time.LocalDate
import java.time.LocalTime

class DataManager {
    companion object{
        var listOfUsers = ArrayList<User>()
        var listOfTurns = ArrayList<Turn>()
        var listOfTurnsFrom = ArrayList<Turn>()
        var listOfTypes = ArrayList<Type>()
        var listOfWorkHours = ArrayList<WorkDayHour>()
        var listOfWaitList = ArrayList<waitListsDB>()
        var listOfHours = ArrayList<Int>()
        var listOfMinutes = ArrayList<Int>()
        var listToPresent = ArrayList<Turn>()
        var listOfReviews = ArrayList<String>()
        var listOfUserTurns = ArrayList<Turn>()
        var listOfStoragePhotos = ArrayList<storageSave>()
        var user: User? = null
    }
    private var firebaseConnections = firebaseConnections()
    private var userRef  = firebaseConnections.initDBConnection("user")
    private var turnsRef = firebaseConnections.initDBConnection("turns")
    private var typesRef = firebaseConnections.initDBConnection("type")
    private var waitListRef = firebaseConnections.initDBConnection("waitList")
    private var workHoursRef = firebaseConnections.initDBConnection("workHours")
    private var reviewRef = firebaseConnections.initDBConnection("reviews")
    private var storageRef = firebaseConnections.initDBConnection("storage")
    private lateinit var database : FirebaseDatabase


    private var listOfUsersFromDB : ArrayList<HashMap<String,String>> = ArrayList()
    private var listOf : ArrayList<User> = ArrayList<User>()
    init {
        if(listOf.isEmpty())
            listOf = ArrayList<User>()
    }

    fun getListOfStoragePhotos() : ArrayList<storageSave> {
        return listOfStoragePhotos
    }

    fun setListOfStoragePhotos(list: ArrayList<storageSave>) {
        listOfStoragePhotos = list
    }

    fun getListOfUsersTurns() : ArrayList<Turn> {
        return listOfUserTurns
    }

    fun getFutureListOfUsersTurns(list: ArrayList<Turn>) : ArrayList<Turn> {
        val listOfFutureTurns = ArrayList<Turn>()
        val currentDate = LocalDate.now()
        val currentTime = LocalTime.now()
        for (turn in listOfUserTurns) {
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

    fun setUser(userr: User?) {
        user = userr
    }

    fun getUser() : User? {
        return user
    }

    fun setListOfUsersTurns(list: ArrayList<Turn>) {
        Log.d("User Turns in DataManager", list.size.toString())
        listOfUserTurns = list
    }

    private fun initDBConnection(path: String) : DatabaseReference {
        database = Firebase.database
        return database.getReference(path)
    }

    fun getListOfUsers(): ArrayList<User> {
        return listOfUsers
    }

    fun getListOfTurns(): ArrayList<Turn> {
        return listOfTurns
    }

    fun getListOfReviews(): ArrayList<String> {
        return listOfReviews
    }

    fun setListOfTurns(turns: ArrayList<Turn>) {
        listOfTurns = turns
    }

    fun getListOfTypes(): ArrayList<Type> {
        return listOfTypes
    }

    fun getWaitListToPresent() : ArrayList<Turn> {
        listToPresent = ArrayList<Turn>()
        for (i in 0..<listOfWaitList.size)
            for (j in 0..<listOfWaitList[i].listOfTurnWaiting.size)
                if(listOfWaitList[i].listOfTurnWaiting[j].user?.userName==user?.userName) {
                    listToPresent.add(Turn(user, listOfWaitList[i].type, listOfWaitList[i].day, listOfWaitList[i].month, listOfWaitList[i].year, listOfHours[listOfWaitList[i].listOfTurnWaiting[j].indexInWaitList], listOfMinutes[listOfWaitList[i].listOfTurnWaiting[j].indexInWaitList],listOfWaitList[i].listOfTurnWaiting[j].indexInWaitList))
                }
        return listToPresent
    }

    fun getFutureListOfTurns(): ArrayList<Turn> {
        val listOfFutureTurns = ArrayList<Turn>()
        val currentDate = LocalDate.now()
        val currentTime = LocalTime.now()
        for (turn in listOfTurns) {
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

    fun getHistoryListOfUsersTurns(): ArrayList<Turn> {
        val listOfHistory = ArrayList<Turn>()
        val currentDate = LocalDate.now()
        val currentTime = LocalTime.now()
        for (turn in listOfUserTurns) {
            if (currentDate.year >= turn.year && currentDate.monthValue == turn.month) {
                if (currentDate.dayOfMonth == turn.day) {
                    if (currentTime.hour == turn.hours) {
                        if (currentTime.minute > turn.minutes) {
                            listOfHistory.add(turn)
                        }
                    } else if (currentTime.hour > turn.hours)
                        listOfHistory.add(turn)
                } else if (currentDate.dayOfMonth > turn.day)
                    listOfHistory.add(turn)
            }
            if(currentDate.year >= turn.year && currentDate.monthValue > turn.month)
                listOfHistory.add(turn)
            if(currentDate.year > turn.year)
                listOfHistory.add(turn)
        }
        listOfHistory.sortWith(compareBy({it.day},{it.month},{it.year},{it.hours},{it.minutes}))
        return listOfHistory
    }

    fun setWaitListToPresent(turn: Turn, listOfWait: Int) {
        for (i in 0..<listOfWaitList.size) {
            if (listOfWaitList[i].day == turn.day && listOfWaitList[i].month == turn.month && listOfWaitList[i].year == turn.year) {
                listOfWaitList[i].waitList[turn.indexInWaitList]--
                if (listOfWaitList[i].listOfTurnWaiting.isNotEmpty()) {
                    for (j in 0..<listOfWaitList[i].listOfTurnWaiting.size) {
                        if (listOfWaitList[i].listOfTurnWaiting[j].day == turn.day && listOfWaitList[i].listOfTurnWaiting[j].month == turn.month && listOfWaitList[i].listOfTurnWaiting[j].year == turn.year && listOfWaitList[i].listOfTurnWaiting[j].hours == turn.hours && listOfWaitList[i].listOfTurnWaiting[j].minutes == turn.minutes) {
                            listOfTurns.add(listOfWaitList[i].listOfTurnWaiting[j])
                            listOfWaitList[i].listOfTurnWaiting.removeAt(j)
                        }
                    }
                }
                waitListRef.setValue(listOfWaitList)
                turnsRef.setValue(listOfTurns)
            }
        }
    }


    fun getListOfWaitList(): ArrayList<waitListsDB> {
        return listOfWaitList
    }

    fun getListOfWorkHours(): ArrayList<WorkDayHour> {
        return listOfWorkHours
    }

    fun setListOfWorkHours(list: ArrayList<WorkDayHour>) {
        listOfWorkHours = list
    }

    fun updateTypesFromDB()
    {
        typesRef.addValueEventListener(object : ValueEventListener { // For realtime data fetching from DB
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value: ArrayList<Type>? = dataSnapshot.getValue(object : GenericTypeIndicator<ArrayList<Type>>() {})
                Log.d("Types in DataManager", value?.size.toString())
                if (value != null) {
                    listOfTypes = value
                }
            }



            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
    }

    fun updatePhotosFromDB()
    {
        storageRef.addValueEventListener(object : ValueEventListener { // For realtime data fetching from DB
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value: ArrayList<storageSave>? = dataSnapshot.getValue(object : GenericTypeIndicator<ArrayList<storageSave>>() {})
                Log.d("Types in DataManager", value?.size.toString())
                if (value != null) {
                    listOfStoragePhotos = value
                }
            }



            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
    }

    fun updateReviewsFromDB()
    {
        listOfReviews = ArrayList()
        reviewRef.addValueEventListener(object : ValueEventListener { // For realtime data fetching from DB
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value: ArrayList<String>? = dataSnapshot.getValue(object : GenericTypeIndicator<ArrayList<String>>() {})
                Log.d("Reviews in DataManager", value?.size.toString())
                if (value != null) {
                    listOfReviews = value
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
    }

    fun updateWaitListFromDB()
    {
        waitListRef.addValueEventListener(object : ValueEventListener { // For realtime data fetching from DB
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value: ArrayList<waitListsDB>? = dataSnapshot.getValue(object : GenericTypeIndicator<ArrayList<waitListsDB>>() {})
                if (value != null) {
                    listOfWaitList = value
                }
            }



            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
    }

    fun updateWorkHoursFromDB()
    {
        workHoursRef.addValueEventListener(object : ValueEventListener { // For realtime data fetching from DB
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value: ArrayList<WorkDayHour>? = dataSnapshot.getValue(object : GenericTypeIndicator<ArrayList<WorkDayHour>>() {})
                if (value != null) {
                    listOfWorkHours = value
                }
                listOfHours.add(listOfWorkHours[0].startHour)
                listOfMinutes.add(0)

                for (i in 1..<listOfWorkHours.size) {
                    listOfHours.add((listOfWorkHours[i-1].startHour + i*1.5).toInt())
                    if(listOfMinutes[i-1] == 0)
                        listOfMinutes.add(30)
                    else
                        listOfMinutes.add(0)
                }
            }



            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
    }

    fun updateUsersFromDB()
    {
        userRef.addValueEventListener(object : ValueEventListener { // For realtime data fetching from DB
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value: ArrayList<User>? = dataSnapshot.getValue(object : GenericTypeIndicator<ArrayList<User>>() {})
                Log.d("Users in DataManager", value?.size.toString())
                if (value != null) {
                    listOfUsers = value
                }
            }



            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
    }

    fun getList(): ArrayList<User>
    {
        return listOf
    }

    fun addUser(user: User){
        listOf.add(user)
        listOf.sortBy { it.name }
    }

    fun getUser(userName: String,password:String) : User?
    {
        for (i in 0..<listOfUsers.size)
        {
            if(listOfUsers[i].userName == userName && listOfUsers[i].password == password)
                return listOfUsers[i]
        }
        return null
    }

    private fun toUser(hashMap: HashMap<String, String>): User {
        val user = User(hashMap["name"].toString(),
            hashMap["mail"].toString(), hashMap["phoneNumber"].toString(),
            hashMap["userName"].toString(), hashMap["password"].toString(), hashMap["token"].toString())
        return user
    }

    fun setList(users: ArrayList<HashMap<String,String>>) {
        listOfUsersFromDB = users
        for (i in 0..<users.size)
        {
            listOf.add(toUser(users[i])!!)
        }
        Log.d("Users", listOfUsers.toString())
    }

    fun getNumOfUsers(): Int {
        return listOf.size
    }

    fun updateTurnsFromDB(user: User) {
        listOfTurns = ArrayList<Turn>()
        turnsRef.addValueEventListener(object : ValueEventListener { // For realtime data fetching from DB
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value: ArrayList<Turn>? = dataSnapshot.getValue(object : GenericTypeIndicator<ArrayList<Turn>>() {})
                //Log.d("Turns in DataManager", value?.size.toString())
                if (value != null) {
                    listOfTurnsFrom = value
                }
                //Log.d("Turns in DataManager1", listOfTurnsFrom.size.toString())
                for (i in 0..<listOfTurnsFrom.size) {
                    if(user.userName == "michaellevi")
                        listOfTurns.add(listOfTurnsFrom[i])
                    if (user.userName == listOfTurnsFrom[i].user?.userName && user.password == listOfTurnsFrom[i].user?.password) {
                        listOfTurns.add(listOfTurnsFrom[i])
                    }
                }
                //Log.d("Turns in DataManager2", listOfTurns.size.toString())
            }



            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
    }

    fun getListOfTurnsForDay(year: Int, month: Int, dayOfMonth: Int): ArrayList<Turn>? {
        val listForEachDay = ArrayList<Turn>()
        for (i in 0..<listOfTurns.size)
            if(listOfTurns[i].year == year && listOfTurns[i].month == month && listOfTurns[i].day == dayOfMonth)
                listForEachDay.add(listOfTurns[i])
        return listForEachDay

    }

    fun setWaitListInWaitList(turn:Turn,listOfTurnWithWaitList: ArrayList<Turn>?) {
        for(i in 0..<listOfWaitList.size) {
            if (listOfWaitList[i].day == turn.day && listOfWaitList[i].month == turn.month && listOfWaitList[i].year == turn.year) {
                listOfWaitList[i].waitList[turn.indexInWaitList]--
                if (listOfTurnWithWaitList != null) {
                    listOfWaitList[i].listOfTurnWaiting = listOfTurnWithWaitList
                }
            }
        }
        waitListRef.setValue(listOfWaitList)
    }

    fun initialUserTurns(list: ArrayList<Turn>) {
        listOfTurns = list
        Log.d("Turns in DataManager initial", listOfTurns.size.toString())
    }

    fun setTurnsAsUser(list: ArrayList<Turn>, user: User): ArrayList<Turn> {
        val listToReturn = ArrayList<Turn>()
        for (i in 0..<list.size) {
            if (list[i].user?.userName == user.userName)
                listToReturn.add(list[i])
        }
        return listToReturn
    }
}