package ru.otus.gateway.registration

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import util.lazyLogger

@Component
class RegistrationClient(
    private val httpClient: HttpClient,
    @Value("\${service.auth.url}")
    private val baseUrl: String
) {

    private val logger by lazyLogger()

    suspend fun register(dto: RegistrationDto): ResponseEntity<String> {
        logger.info("Registration client request. Start: $dto")
        return try {
            val response = httpClient.post("$baseUrl/registration") {
                contentType(ContentType.Application.Json)
                setBody(dto)
            }
            val body = response.bodyAsText()
            val status = response.status
            logger.info("Registration result: $status, $body")
            ResponseEntity.status(status.value).body(body)
        } catch (ex: Exception) {
            logger.error("Registration exception", ex)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed")
        }
    }
}
