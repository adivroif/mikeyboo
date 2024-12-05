package com.example.mikeyboo.models

class waitListsDB (
    var waitList: MutableList<Int>,
    var day : Int,
    var month : Int,
    var year : Int,
    var type : String,
    var listOfTurnWaiting : ArrayList<Turn>

){
    constructor() : this(MutableList<Int>(6){0},0,0,0," ",ArrayList<Turn>())
}
