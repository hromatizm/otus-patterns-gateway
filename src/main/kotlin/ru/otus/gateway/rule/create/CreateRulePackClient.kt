package ru.otus.gateway.rule.create

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import ru.otus.gateway.rule.RulePackDto
import ru.otus.gateway.util.lazyLogger

@Component
class CreateRulePackClient(
    private val httpClient: HttpClient,
    @Value("\${services.redirect}")
    private val baseUrl: String
) {

    private val logger by lazyLogger()
    private val rulePackUrl = "$baseUrl/rule-pack"

    suspend fun create(dto: RulePackDto): ResponseEntity<Any> {
        logger.info("Create rule pack client request. Start: $dto")
        return try {
            val response = httpClient.post("$rulePackUrl/create") {
                contentType(ContentType.Application.Json)
                setBody(dto)
            }
            val body = response.bodyAsText()
            val status = response.status
            logger.info("Create rule pack result: $status, $body")
            ResponseEntity.status(status.value).body(body)
        } catch (ex: Exception) {
            logger.error("Create rule pack exception", ex)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Create rule pack failed")
        }
    }

}
