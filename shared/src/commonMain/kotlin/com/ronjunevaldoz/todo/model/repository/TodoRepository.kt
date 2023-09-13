package com.ronjunevaldoz.todo.model.repository

import com.ronjunevaldoz.todo.database.realm
import com.ronjunevaldoz.todo.model.data.Todo
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.query.RealmSingleQuery
import org.mongodb.kbson.ObjectId

/**
 * TODO Move to dependency injector like koin
 */
object TodoRepository {
    suspend fun add(todo: Todo) {
        realm.write {
            copyToRealm(todo)
        }
    }

    suspend fun update(todoId: String, block: Todo.() -> Unit) {
        realm.write {
            query<Todo>("_id = $0", ObjectId(todoId)).first().find()!!.also { todo ->
                todo.apply(block)
            }
        }
    }

    suspend fun delete(todo: Todo) {
        realm.write {
            query<Todo>("_id = $0", todo._id).first().find()?.also { delete(it) }
        }
    }

    fun findTodo(todoId: String): RealmSingleQuery<Todo> {
        return realm.query<Todo>("_id = $0", ObjectId(todoId)).first()
    }

    fun findTodos(userId: String? = null): RealmResults<Todo> {
        if (userId == null) {
            return realm.query<Todo>().find()
        }
        return realm.query<Todo>("userId = $0", ObjectId(userId)).find()
    }
}