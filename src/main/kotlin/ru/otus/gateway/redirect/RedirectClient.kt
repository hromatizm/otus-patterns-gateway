package ru.otus.gateway.redirect

import com.sun.jndi.toolkit.url.Uri
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import ru.otus.gateway.util.lazyLogger
import org.springframework.http.HttpHeaders
import java.net.URI

@Component
class RedirectClient(
    private val httpClient: HttpClient,
    @Value("\${services.redirect}")
    private val baseUrl: String
) {

    private val logger by lazyLogger()

    suspend fun getLink(uri: String, args: Map<String, String>): ResponseEntity<Any> {
        logger.info("Redirect client request. Start: $args")
        return try {
            val response = httpClient.post("$baseUrl/smart-link/$uri") {
                contentType(ContentType.Application.Json)
                setBody(args)
            }
            val body = response.bodyAsText()
            val status = response.status
            logger.info("Redirect result: $status, $body")
            if (status.isSuccess()) {
                ResponseEntity
                    .status(HttpStatus.PERMANENT_REDIRECT)
                    .location(URI.create(body))
                    .build()
            } else {
                ResponseEntity.status(status.value).body(body)
            }
        } catch (ex: Exception) {
            logger.error("Redirect exception", ex)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Redirect failed")
        }
    }

}
