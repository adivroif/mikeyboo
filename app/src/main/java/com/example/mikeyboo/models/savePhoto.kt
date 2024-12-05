package com.example.mikeyboo.models

import com.google.firebase.storage.StorageReference

class savePhoto(
    var ref: String?,
    var turn: Turn
)
{
    constructor() : this(null, Turn())
}
