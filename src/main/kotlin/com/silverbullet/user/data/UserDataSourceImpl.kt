package com.silverbullet.user.data

import com.silverbullet.user.model.User
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class UserDataSourceImpl(
    db: CoroutineDatabase
) : UserDataSource {

    private val usersCollection = db.getCollection<User>()

    override suspend fun getUserByUsername(username: String): User? {
        return usersCollection
            .findOne(User::username eq username)
    }

    override suspend fun insertUser(user: User): Boolean {
        return usersCollection
            .insertOne(user)
            .wasAcknowledged()
    }
}