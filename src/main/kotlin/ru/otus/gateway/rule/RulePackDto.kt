package ru.otus.gateway.rule

data class RulePackDto(
    val id: Long? = null,
    val uri: String,
    val pack: List<Map<String, String>> = emptyList(),
)