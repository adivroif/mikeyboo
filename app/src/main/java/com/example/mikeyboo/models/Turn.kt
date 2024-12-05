package com.example.mikeyboo.models

class Turn(
    var user: User?,
    var type: String,
    var day: Int,
    var month: Int,
    var year: Int,
    var hours: Int,
    var minutes: Int,
    var indexInWaitList: Int
){
    constructor() : this(null, "", 0, 0, 0, 0, 0,0)
}
