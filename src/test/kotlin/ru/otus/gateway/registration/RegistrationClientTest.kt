package ru.otus.gateway.registration

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

class RegistrationClientTest {

    private val userId = "user123"
    private val baseUrl = "http://localhost:8092/api/v1"

    @Test
    fun `registration client should return user id`() = runTest {
        // Arrange
        val mockEngine = MockEngine { request ->
            respond(
                content = userId,
                status = HttpStatusCode.OK,
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
        val registrationClient = RegistrationClient(client, baseUrl)
        val registrationDto = RegistrationDto(fullName = "testFullName", login = "testLogin", password = "testPassword")

        // Act
        val responseEntity = registrationClient.register(registrationDto)

        // Assert
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(responseEntity.body).isEqualTo(userId)
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

        val registrationClient = RegistrationClient(client, baseUrl)
        val registrationDto = RegistrationDto(fullName = "testFullName", login = "testLogin", password = "testPassword")

        // Act
        val responseEntity = registrationClient.register(registrationDto)

        // Assert
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        assertThat(responseEntity.body).isEqualTo("Registration failed")
    }
}
