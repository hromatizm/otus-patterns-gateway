package ru.otus.gateway.login

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

class LoginClientTest {

    private val jwt = "testJwt"
    private val baseUrl = "http://localhost:8092/api/v1"

    @Test
    fun `login client should return jwt token`() = runTest {
        // Arrange
        val mockEngine = MockEngine { request ->
            respond(
                content = jwt,
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
        val loginClient = LoginClient(client, baseUrl)
        val loginDto = LoginDto(login = "testLogin", password = "testPassword")

        // Act
        val responseEntity = loginClient.login(loginDto)

        // Assert
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(responseEntity.body).isEqualTo(jwt)
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

        val loginClient = LoginClient(client, baseUrl)
        val loginDto = LoginDto(login = "testLogin", password = "testPassword")

        // Act
        val responseEntity = loginClient.login(loginDto)
        // Assert
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        assertThat(responseEntity.body).isEqualTo("Login failed")
    }
}
