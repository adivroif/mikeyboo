package com.example.mikeyboo.callbacks

import com.example.mikeyboo.models.Turn

interface CallbackTurn {
    fun turnClicked(turn: Turn, position: Int)
}