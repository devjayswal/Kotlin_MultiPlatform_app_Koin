package com.example.test1.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String = "",
    val age: Int = 0,
    val city: String = "",
    val address: String = "",
    val phone: String = "",
    val email: String = "",
    val isStudent: Boolean = false,
    val grades: List<Int> = emptyList(),
    val relatives: List<User> = emptyList()
)