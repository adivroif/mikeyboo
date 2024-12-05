package com.example.mikeyboo.callbacks

import com.example.mikeyboo.models.Turn

interface CallbackType {
    fun typeClicked(type: String, position: Int)
}