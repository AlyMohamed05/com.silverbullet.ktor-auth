package com.silverbullet.user.data

import com.silverbullet.user.data.model.User

interface UserDataSource {

    suspend fun getUserByUsername(username: String): User?

    /**
     * @return boolean to identify if user is inserted or not.
     */
    suspend fun insertUser(user: User): Boolean
}