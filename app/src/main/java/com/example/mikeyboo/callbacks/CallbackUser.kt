package com.example.mikeyboo.callbacks

import com.example.mikeyboo.models.User
import com.example.mikeyboo.models.waitListsDB

interface CallbackUser {
    fun userClicked(user: User, position: Int)

}