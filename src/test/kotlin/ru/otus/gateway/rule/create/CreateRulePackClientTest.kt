package ru.otus.gateway.rule.create

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
import ru.otus.gateway.rule.RulePackDto

class CreateRulePackClientTest {

    private val rulePack = """{"rule": "pack"}"""
    private val baseUrl = "http://localhost:8092/api/v1"

    @Test
    fun `create rule pack client should return rule pack`() = runTest {
        // Arrange
        val mockEngine = MockEngine { request ->
            respond(
                content = rulePack,
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
        val createRulePackClient = CreateRulePackClient(client, baseUrl)
        val rulePackDto = RulePackDto(uri = "someUri")

        // Act
        val responseEntity = createRulePackClient.create(rulePackDto)

        // Assert
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(responseEntity.body).isEqualTo(rulePack)
    }

    @Test
    fun `exception when create rule should return 500`() = runTest {
        // Arrange
        val mockEngine = MockEngine {
            throw RuntimeException()
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { prettyPrint = true })
            }
        }

        val createRulePackClient = CreateRulePackClient(client, baseUrl)
        val rulePackDto = RulePackDto(uri = "someUri")

        // Act
        val responseEntity = createRulePackClient.create(rulePackDto)

        // Assert
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        assertThat(responseEntity.body).isEqualTo("Create rule pack failed")
    }
}
