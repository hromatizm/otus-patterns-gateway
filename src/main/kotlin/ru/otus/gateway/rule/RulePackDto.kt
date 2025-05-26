package ru.otus.gateway.rule

import kotlinx.serialization.Serializable

@Serializable
data class RulePackDto(
    val id: Long? = null,
    val uri: String,
    val pack: List<Map<String, String>> = emptyList(),
)