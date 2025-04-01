package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
    val email: String,
)