package com.ronjunevaldoz.todo.database

import com.ronjunevaldoz.todo.model.data.Todo
import com.ronjunevaldoz.todo.model.data.User
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

val realm by lazy {
    val config = RealmConfiguration.create(setOf(
        Todo::class,
        User::class
    ))
    Realm.open(config)
}