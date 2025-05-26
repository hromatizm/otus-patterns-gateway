package ru.otus.gateway.redirect

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test-user")
class RedirectControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var redirectClientMock: RedirectClient

    private val redirectUrl = "www.redirect.url"
    private val initialUri = "initial.uri"
    private val requestArgs = mapOf(
        "Accept-Language" to "ru"
    )

    @BeforeEach
    fun setupMocks() {
        coEvery { redirectClientMock.getLink(initialUri, requestArgs) }.returns(
            ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).body(redirectUrl)
        )
    }

    @Test
    fun `the answer is OK`() {
        // Arrange
        val request = prepareRedirectRequest(HttpHeaders().apply { add("Accept-Language", "ru") })

        // Act
        val resultActions: ResultActions = mockMvc.perform(request)

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isPermanentRedirect)
    }

    @Test
    fun `body with redirect url`() {
        // Arrange
        val request = prepareRedirectRequest(HttpHeaders().apply { add("Accept-Language", "ru") })

        // Act
        val responseContent = mockMvc
            .perform(request)
            .andReturn().response.contentAsString

        // Assert
        Assertions.assertThat(responseContent).isEqualTo(redirectUrl)
    }

    private fun prepareRedirectRequest(headers: HttpHeaders): MockHttpServletRequestBuilder {
        return get("/api/v1/smart-link/$initialUri")
            .headers(headers)
    }
}
