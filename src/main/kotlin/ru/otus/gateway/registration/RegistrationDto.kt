package ru.otus.gateway.registration

data class RegistrationDto(
    val fullName: String,
    val login: String,
    val password: String
)