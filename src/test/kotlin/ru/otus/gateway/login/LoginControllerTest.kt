package ru.otus.gateway.login

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

private const val JWT_TOKEN = "jwt_token"
private const val USER_LOGIN = "user_login"
private const val USER_PASSWORD = "user_password"

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var loginClientMock: LoginClient

    private val uri = "/api/v1/login"
    private val loginDto = LoginDto(
        login = USER_LOGIN,
        password = USER_PASSWORD
    )

    @BeforeEach
    fun setupMocks() {
        coEvery { loginClientMock.login(loginDto) }.returns(
            ResponseEntity.ok(JWT_TOKEN)
        )
    }

    @Test
    fun `the answer is OK`() {
        // Arrange
        val request = createLoginRequest(loginDto)

        // Act
        val resultActions: ResultActions = mockMvc.perform(request)

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `body with user id`() {
        // Arrange
        val request = createLoginRequest(loginDto)

        // Act
        val responseContent = mockMvc
            .perform(request)
            .andReturn().response.contentAsString

        // Assert
        Assertions.assertThat(responseContent).isEqualTo(JWT_TOKEN)
    }

    private fun createLoginRequest(dto: LoginDto): MockHttpServletRequestBuilder {
        val messageJson = objectMapper.writeValueAsString(dto)
        return MockMvcRequestBuilders.post(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .content(messageJson)
    }

}