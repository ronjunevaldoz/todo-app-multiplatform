package com.ronjunevaldoz.todo.model.repository

import com.ronjunevaldoz.todo.database.realm
import com.ronjunevaldoz.todo.model.data.User
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmSingleQuery
import org.mongodb.kbson.ObjectId

/**
 * TODO Move to dependency injector like koin
 */
object UserRepository {
    suspend fun add(user: User) {
        realm.write {
            copyToRealm(user)
        }
    }

    suspend fun update(userId: String, block: User.() -> Unit) =
        realm.write {
            query<User>("_id = $0", ObjectId(userId)).first().find()!!.also { user ->
                user.apply(block)
            }
        }

    suspend fun delete(user: User) {
        realm.write {
            query<User>("_id = $0", user._id).first().find()?.also { delete(it) }
        }
    }

    fun findUser(userId: String): RealmSingleQuery<User> {
        return realm.query<User>("_id = $0", ObjectId(userId)).first()
    }

    fun findUser(username: String, password: String): RealmSingleQuery<User> {
        return realm.query<User>("username = $0 AND password = $1", username, password).first()
    }

    fun findUsers() = realm.query<User>().find()

    fun getCurrentUser(): User? {
        return findUsers().find { it.isAuthenticated }
    }

    suspend fun logout() {
        realm.write {
            query<User>( ).find().forEach {
                it.apply {
                    isAuthenticated = false
                }
            }
        }
    }
}