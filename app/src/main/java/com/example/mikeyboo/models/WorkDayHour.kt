package com.example.mikeyboo.models

class WorkDayHour (
    var startHour : Int,
    var endHour : Int,
    var startBreakTime : Int,
    var endBreakTime : Int
){
    constructor() : this(0,0,0,0)
}