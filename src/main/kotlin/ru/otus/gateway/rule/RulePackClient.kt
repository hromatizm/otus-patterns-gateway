package ru.otus.gateway.rule

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
class RulePackClient(
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

    suspend fun update(dto: RulePackDto): ResponseEntity<Any> {
        logger.info("Update rule pack client request. Start: $dto")
        return try {
            val response = httpClient.put("$rulePackUrl/update") {
                contentType(ContentType.Application.Json)
                setBody(dto)
            }
            val body = response.bodyAsText()
            val status = response.status
            logger.info("Update rule pack result: $status, $body")
            ResponseEntity.status(status.value).body(body)
        } catch (ex: Exception) {
            logger.error("Update rule pack exception", ex)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update rule pack failed")
        }
    }

    suspend fun delete(uri: String): ResponseEntity<Any> {
        logger.info("Delete rule pack client request. Start: $uri")
        return try {
            val response = httpClient.delete("$rulePackUrl/delete") {
                url { parameters.append("uri", uri) }
            }
            val body = response.bodyAsText()
            val status = response.status
            logger.info("Delete rule pack result: $status, $body")
            ResponseEntity.status(status.value).body(body)
        } catch (ex: Exception) {
            logger.error("Delete rule pack exception", ex)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete rule pack failed")
        }
    }

}
