package ru.otus.gateway.rule.get

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.otus.gateway.rule.RulePackDto
import java.net.URI

private const val INITIAL_URI = "/patterns-course"
private const val PACK_ID = 1234567890L

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test-user")
class GetRulePackControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var getClientMock: GetRulePackClient

    private val uri = URI("/api/v1/rule-pack/get")

    private val rulePackDto = RulePackDto(
        id = PACK_ID,
        uri = INITIAL_URI,
        pack = listOf(
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
    )

    @BeforeEach
    fun setupMocks() {
        coEvery { getClientMock.get(INITIAL_URI) }.returns(
            ResponseEntity.ok(rulePackDto)
        )
    }

    @Test
    fun `the answer is OK`() {
        // Arrange
        val request = prepareGetRequest(uri = uri, param = INITIAL_URI)

        // Act
        val resultActions: ResultActions = mockMvc.perform(request)

        // Assert
        resultActions.andExpect(status().isOk)
    }

    @Test
    fun `body with pack`() {
        // Arrange
        val request = prepareGetRequest(uri = uri, param = INITIAL_URI)

        // Act
        val content = mockMvc
            .perform(request)
            .andReturn().response.contentAsString
        val resultDto = objectMapper.readValue(content, RulePackDto::class.java)

        // Assert
        assertThat(resultDto).isEqualTo(rulePackDto)
    }

    private fun prepareGetRequest(uri: URI, param: String): MockHttpServletRequestBuilder {
        return get(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .param("uri", param)
    }

    private fun prepareDeleteRequest(uri: URI, param: String): MockHttpServletRequestBuilder {
        return delete(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .param("uri", param)
    }
}