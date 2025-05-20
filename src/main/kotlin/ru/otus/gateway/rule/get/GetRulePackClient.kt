package ru.otus.gateway.rule.get

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import ru.otus.gateway.util.lazyLogger

@Component
class GetRulePackClient(
    private val httpClient: HttpClient,
    @Value("\${services.redirect}")
    private val baseUrl: String
) {

    private val logger by lazyLogger()
    private val rulePackUrl = "$baseUrl/rule-pack"

    suspend fun get(uri: String): ResponseEntity<Any> {
        logger.info("Get rule pack client request. Start: $uri")
        return try {
            val response = httpClient.get("$rulePackUrl/get") {
                url { parameters.append("uri", uri) }
            }
            val body = response.bodyAsText()
            val status = response.status
            logger.info("Get rule pack result: $status, $body")
            ResponseEntity.status(status.value).body(body)
        } catch (ex: Exception) {
            logger.error("Get rule pack exception", ex)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Get rule pack failed")
        }
    }

}
