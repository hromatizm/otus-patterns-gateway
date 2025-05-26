package ru.otus.gateway.registration

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

private const val USER_ID = "1234567890"
private const val USER_FULL_NAME = "user_full_name"
private const val USER_LOGIN = "user_login"
private const val USER_PASSWORD = "user_password"

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RegistrationControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var registrationClientMock: RegistrationClient

    private val uri = "/api/v1/registration"
    private val registrationDto = RegistrationDto(
        fullName = USER_FULL_NAME,
        login = USER_LOGIN,
        password = USER_PASSWORD
    )

    @BeforeEach
    fun setupMocks() {
        coEvery { registrationClientMock.register(registrationDto) }.returns(
            ResponseEntity.ok(USER_ID)
        )
    }

    @Test
    fun `the answer is OK`() {
        // Arrange
        val request = createRegistrationRequest(registrationDto)

        // Act
        val resultActions: ResultActions = mockMvc.perform(request)

        // Assert
        resultActions.andExpect(status().isOk)
    }

    @Test
    fun `body with user id`() {
        // Arrange
        val request = createRegistrationRequest(registrationDto)

        // Act
        val responseContent = mockMvc
            .perform(request)
            .andReturn().response.contentAsString

        // Assert
        assertThat(responseContent).isEqualTo(USER_ID.toString())
    }

    private fun createRegistrationRequest(dto: RegistrationDto): MockHttpServletRequestBuilder {
        val messageJson = objectMapper.writeValueAsString(dto)
        return post(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .content(messageJson)
    }

}
