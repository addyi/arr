package com.example.route.users.model

import kotlinx.serialization.Serializable

@Serializable
data class RequestUser(
    val name: Name,
    val email: Email,
    val age: Age,
)

@Serializable
data class ResponseUser(
    val id: String,
    val name: Name,
    val email: Email,
    val age: Age,
)

@Serializable
@JvmInline
value class Email(val value: String) {
    init {
        require(value.isNotEmpty()) { "Email cannot be empty" }
        require(value.contains("@")) { "Email must contain @" }
    }
}

@Serializable
@JvmInline
value class Name(val value: String) {
    init {
        require(value.isNotEmpty()) { "Name cannot be empty" }
        require(value.length > 2) { "Name must be longer than 2 characters" }
    }
}

@Serializable
@JvmInline
value class Age(val value: Int) {
    init {
        require(value > 0) { "Age must be greater than 0" }
        require(value < 150) { "Age must be less than 150" }
    }
}


@Serializable
data class Users(val users: List<ResponseUser>)
