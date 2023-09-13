package com.ronjunevaldoz.todo.model.data

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class User : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var username: String = ""
    var password: String = ""
    var isAuthenticated: Boolean = false

    @Ignore
    val id: String
        get() {
            return _id.toHexString()
        }
}