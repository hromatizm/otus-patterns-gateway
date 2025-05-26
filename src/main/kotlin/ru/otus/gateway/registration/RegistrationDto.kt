package ru.otus.gateway.registration

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationDto(
    val fullName: String,
    val login: String,
    val password: String
)