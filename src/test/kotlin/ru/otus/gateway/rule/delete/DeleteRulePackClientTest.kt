package ru.otus.gateway.rule.delete

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.engine.mock.MockEngine.Companion.invoke
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class DeleteRulePackClientTest {

    private val baseUrl = "http://localhost:8092/api/v1"

    @Test
    fun `delete rule pack client should return status 200`() = runTest {
        // Arrange
        val mockEngine = MockEngine { request ->
            respond(
                content = "",
                status = HttpStatusCode.OK,
            )
        }
        val client = HttpClient(mockEngine)
        val deleteRulePackClient = DeleteRulePackClient(client, baseUrl)

        // Act
        val responseEntity = deleteRulePackClient.delete("someUri")

        // Assert
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
    }


    @Test
    fun `exception when delete rule should return 500`() = runTest {
        // Arrange
        val mockEngine = MockEngine {
            throw RuntimeException()
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { prettyPrint = true })
            }
        }

        val deleteRulePackClient = DeleteRulePackClient(client, baseUrl)

        // Act
        val responseEntity = deleteRulePackClient.delete("someUri")

        // Assert
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        assertThat(responseEntity.body).isEqualTo("Delete rule pack failed")
    }
}
