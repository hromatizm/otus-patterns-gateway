package ru.otus.gateway.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(
    val login: String,
    val password: String
)