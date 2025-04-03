package com.example.users

import com.example.dto.Age
import com.example.dto.Email
import com.example.dto.Name
import com.example.dto.User

class FakeUserRepository : UserRepository {

    private val users = mutableListOf(
        User(Name("Anton"), Email("anton@example.com"), Age(8)),
        User(Name("Arne"), Email("arne@example.com"), Age(12)),
        User(Name("Danil"), Email("danil@example.com"), Age(14)),
        User(Name("Friederike"), Email("friederike@example.com"), Age(16)),
        User(Name("Iwo"), Email("iwo@example.com"), Age(18)),
        User(Name("James"), Email("james@example.com"), Age(20)),
        User(Name("Johannes"), Email("johannes@example.com"), Age(10)),
        User(Name("Taras"), Email("taras@example.com"), Age(22)),
        User(Name("Vladimir"), Email("vladimir@examle.com"), Age(24)),
    )

    override fun createUser(user: User): Boolean {
        return users.add(user)
    }

    override fun getUserById(id: Int): User? {
        return try {
            users[id]
        } catch (ex: IndexOutOfBoundsException) {
            null
        }
    }

    override fun getAllUsers(): List<User> {
        return users
    }

    override fun getUserByEmail(email: Email): User? {
        return users.find { it.email.value == email.value }
    }

    override fun getUsersByAge(age: Age): List<User> {
        return users.filter { it.age.value == age.value }
    }

    override fun updateUser(id: Int, user: User): Boolean {
        return try {
            users[id] = user
            true
        } catch (ex: IndexOutOfBoundsException) {
            false
        }
    }

    override fun deleteUser(id: Int): Boolean {
        return try {
            users.removeAt(id)
            true
        } catch (ex: IndexOutOfBoundsException) {
            false
        }
    }

    override fun deleteUser(email: Email): Boolean {
        return try {
            users.removeAll { user -> user.email.value == email.value }
        } catch (ex: IndexOutOfBoundsException) {
            false
        }
    }


    override fun deleteAllUsers(): Boolean {
        users.clear()
        return true
    }
}