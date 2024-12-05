package com.example.mikeyboo

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class FirebaseID : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d("TAG", "Refreshed token: $token")
    }
}