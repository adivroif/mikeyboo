package com.example.mikeyboo.callbacks

import com.example.mikeyboo.models.Turn

interface CallbackWait {
    fun turnClicked(turn: Turn, position: Int)
}