package ru.otus.gateway.redirect

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.engine.mock.MockEngine.Companion.invoke
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class RedirectClientTest {

    private val redirectUrl = "www.redirect.url"
    private val initialUri = "initial.uri"
    private val baseUrl = "http://localhost:8092/api/v1"
    private val requestArgs = mapOf<String, String>()

    @Test
    fun `redirect client should return url`() = runTest {
        // Arrange
        val mockEngine = MockEngine { request ->
            respond(
                content = redirectUrl,
                status = HttpStatusCode.PermanentRedirect,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }
        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                    }
                )
            }
        }
        val redirectClient = RedirectClient(client, baseUrl)

        // Act
        val responseEntity = redirectClient.getLink(uri = initialUri, args = requestArgs)

        // Assert
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.PERMANENT_REDIRECT)
        assertThat(responseEntity.body).isEqualTo(redirectUrl)
    }

    @Test
    fun `exception should return 500`() = runTest {
        // Arrange
        val mockEngine = MockEngine {
            throw RuntimeException()
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { prettyPrint = true })
            }
        }

        val redirectClient = RedirectClient(client, baseUrl)

        // Act
        val responseEntity = redirectClient.getLink(uri = initialUri, args = requestArgs)

        // Assert
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        assertThat(responseEntity.body).isEqualTo("Redirect failed")
    }

}
