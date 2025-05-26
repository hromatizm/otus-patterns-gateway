package ru.otus.gateway.rule.delete

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.net.URI

private const val INITIAL_URI = "/patterns-course"

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test-user")
class DeleteRulePackControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var deleteClientMock: DeleteRulePackClient

    private val uri = URI("/api/v1/rule-pack/delete")

    @BeforeEach
    fun setupMocks() {
        coEvery { deleteClientMock.delete(INITIAL_URI) }.returns(
            ResponseEntity.ok("")
        )
    }

    @Test
    fun `the answer is OK`() {
        // Arrange
        val request = prepareDeleteRequest(uri = uri, param = INITIAL_URI)

        // Act
        val resultActions: ResultActions = mockMvc.perform(request)

        // Assert
        resultActions.andExpect(status().isOk)
    }

    private fun prepareDeleteRequest(uri: URI, param: String): MockHttpServletRequestBuilder {
        return delete(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .param("uri", param)
    }
}