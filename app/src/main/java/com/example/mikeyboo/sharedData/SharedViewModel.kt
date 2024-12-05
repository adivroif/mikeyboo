package com.example.mikeyboo.sharedData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mikeyboo.models.Turn
import com.example.mikeyboo.models.Type
import com.example.mikeyboo.models.User
import com.example.mikeyboo.models.waitListsDB
import java.time.LocalDate
import java.time.LocalTime


class SharedViewModel() : ViewModel() {
    companion object {
        private var listOfType = ArrayList<Type>()
        private val sharedData = MutableLiveData<String>()
        private var listOfUsers = ArrayList<User>()
        private var listOfTurns = ArrayList<Turn>()
        private lateinit var listOfHistoryTurns : ArrayList<Turn>
        private lateinit var listOfFutureTurns : ArrayList<Turn>
        private lateinit var listOfWaitTurns : ArrayList<Turn>
        private var listOfWaitList = ArrayList<waitListsDB>()
        private var waitList = MutableList(6){0}
        private var dataManager = DataManager()
        private var user:User? = null
    }
    fun getData(): String? {
        return sharedData.value
    }

    fun getListOfType(): ArrayList<Type> {
        return listOfType
    }

    fun setListOfType(list:ArrayList<Type>)
    {
        listOfType = list
    }

    fun getListOfUsers(): ArrayList<User> {
        return listOfUsers
    }
    fun setListOfUsers(list: ArrayList<User>) {
        listOfUsers = list
    }

    fun getListOfTurns(): ArrayList<Turn> {
        return listOfTurns
    }

    fun getWaitList(): MutableList<Int> {
        return waitList
    }

    fun setWaitList(list: MutableList<Int>){
        waitList = list
    }

    fun setListOfWaitList(list: ArrayList<waitListsDB>){
        listOfWaitList = list
    }

    fun setIndexOfWaitList(index: Int) {
        waitList[index]++
    }

    fun getHistoryListOfTurns(): ArrayList<Turn> {
        listOfHistoryTurns = ArrayList<Turn>()
        listOfTurns = dataManager.getListOfUsersTurns()
        val currentDate = LocalDate.now()
        val currentTime = LocalTime.now()
        for (turn in listOfTurns) {
            if (currentDate.year >= turn.year && currentDate.monthValue == turn.month) {
                if (currentDate.dayOfMonth == turn.day) {
                    if (currentTime.hour == turn.hours) {
                        if (currentTime.minute > turn.minutes) {
                            listOfHistoryTurns.add(turn)
                        }
                    } else if (currentTime.hour > turn.hours)
                        listOfHistoryTurns.add(turn)
                } else if (currentDate.dayOfMonth > turn.day)
                    listOfHistoryTurns.add(turn)
            }
            if(currentDate.year >= turn.year && currentDate.monthValue > turn.month)
                listOfHistoryTurns.add(turn)
            if(currentDate.year > turn.year)
                listOfHistoryTurns.add(turn)
        }
        listOfHistoryTurns.sortWith(compareBy({it.day},{it.month},{it.year},{it.hours},{it.minutes}))
        return listOfHistoryTurns
    }

    fun setHistoryListOfTurns(list: ArrayList<Turn>) {
        listOfHistoryTurns = list
    }

    fun setFutureListOfTurns(list: ArrayList<Turn>) {
        listOfFutureTurns = list
    }

    fun setWaitListTurns(list: ArrayList<waitListsDB>) {
        listOfWaitList = list
    }

    fun getFutureListOfTurns(): ArrayList<Turn> {
        listOfFutureTurns = ArrayList<Turn>()
        listOfTurns = dataManager.getListOfUsersTurns()
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

    fun getWaitListTurns(): ArrayList<waitListsDB> {
        listOfWaitList.sortWith(compareBy({it.day},{it.month},{it.year}))
        return listOfWaitList
    }

    fun setData(data: String) {
        sharedData.value = data
    }

    fun setListOfTurns(turns: ArrayList<Turn>) {
        listOfTurns = turns
        setFutureListOfTurns(getFutureListOfTurns())
        setHistoryListOfTurns(getHistoryListOfTurns())
        setWaitListTurns(getWaitListTurns())
    }

    fun setUser(userTo: User?) {
        user = userTo
    }

    fun getUser(): User? {
        return user
    }

}