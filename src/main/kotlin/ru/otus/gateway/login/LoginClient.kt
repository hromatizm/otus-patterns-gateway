package ru.otus.gateway.login

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import ru.otus.gateway.util.lazyLogger

@Component
class LoginClient(
    private val httpClient: HttpClient,
    @Value("\${service.auth.url}")
    private val baseUrl: String
) {

    private val logger by lazyLogger()

    suspend fun login(dto: LoginDto): ResponseEntity<String> {
        logger.info("Login client request. Start: $dto")
        return try {
            val response = httpClient.post("$baseUrl/login") {
                contentType(ContentType.Application.Json)
                setBody(dto)
            }
            val body = response.bodyAsText()
            val status = response.status
            logger.info("Login result: $status, $body")
            ResponseEntity.status(status.value).body(body)
        } catch (ex: Exception) {
            logger.error("Login exception", ex)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed")
        }
    }
}
