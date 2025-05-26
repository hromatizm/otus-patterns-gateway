package ru.otus.gateway.rule.create

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.otus.gateway.rule.RulePackDto
import java.net.URI
import kotlin.test.Test
import kotlin.to

private const val INITIAL_URI = "/patterns-course"
private const val PACK_ID = 1234567890L

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test-user")
class CreateRulePackControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: com.fasterxml.jackson.databind.ObjectMapper

    @MockkBean
    lateinit var createClientMock: CreateRulePackClient

    private val pack = listOf(
        mapOf(
            "lang" to "ru",
            "name" to "test_name_1",
            "redirect" to "https://otus.ru/test_redirect_1"
        ),
        mapOf(
            "lang" to "en",
            "name" to "test_name_2",
            "redirect" to "https://otus.ru/test_redirect_2"
        )
    )

    private val uri = URI("/api/v1/rule-pack/create")

    private val rulePackDto = RulePackDto(
        uri = INITIAL_URI,
        pack = pack
    )

    @BeforeEach
    fun setupMocks() {
        coEvery { createClientMock.create(rulePackDto) }.returns(
            ResponseEntity.ok(rulePackDto.copy(id = PACK_ID))
        )
    }

    @Test
    fun `the answer is OK`() {
        // Arrange
        val request = preparePostRequest(uri = uri, body = rulePackDto)

        // Act
        val resultActions: ResultActions = mockMvc.perform(request)

        // Assert
        resultActions.andExpect(status().isOk)
    }

    @Test
    fun `body with pack`() {
        // Arrange
        val targetDto = rulePackDto.copy(id = PACK_ID)
        val request = preparePostRequest(uri = uri, body = rulePackDto)

        // Act
        val content = mockMvc
            .perform(request)
            .andReturn().response.contentAsString
        val resultDto = objectMapper.readValue(content, RulePackDto::class.java)

        // Assert
        assertThat(resultDto).isEqualTo(targetDto)
    }

    private fun preparePostRequest(uri: URI, body: Any): MockHttpServletRequestBuilder {
        val messageJson = objectMapper.writeValueAsString(body)
        return post(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .content(messageJson)
    }

}