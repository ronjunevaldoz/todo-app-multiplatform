package com.ronjunevaldoz.todo.model.data

import com.ronjunevaldoz.todo.utils.toInstant
import com.ronjunevaldoz.todo.utils.toRealmInstant
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.datetime.Instant
import org.mongodb.kbson.ObjectId

class Todo : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var title: String = ""
    var description: String = ""
    var completed: Boolean = false
    private var _priority: Int = Priority.LOW.value
    private var _dueDateTime = RealmInstant.now()

    @Ignore
    var dueDateTime: Instant
        get() {
            return _dueDateTime.toInstant()
        }
        set(value) {
            _dueDateTime = value.toRealmInstant()
        }

    @Ignore
    val id: String
        get() {
            return _id.toHexString()
        }

    @Ignore
    var priority: Priority
        get() {
            // because state is actually a String and another client could assign an invalid value,
            // default the state to "TADPOLE" if the state is unreadable
            return try {
                // fetches the FrogState enum value associated with the current internal string value
                Priority.entries.find { it.value == _priority } ?: Priority.UNKNOWN
            } catch (e: IllegalArgumentException) {
                Priority.UNKNOWN
            }
        }
        set(value) {
            // users set state using a FrogState, but it is saved as a string internally
            _priority = value.value
        }

    val status: String
        get() = if(completed) "Completed" else "Todo"
}