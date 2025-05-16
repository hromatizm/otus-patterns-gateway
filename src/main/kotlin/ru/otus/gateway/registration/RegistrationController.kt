package ru.otus.gateway.registration

import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.otus.gateway.util.lazyLogger

@RestController
@RequestMapping("/api/v1")
class RegistrationController(
    private val registrationClient: RegistrationClient,
) {

    private val logger by lazyLogger()

    @PostMapping("/registration")
    fun register(@RequestBody dto: RegistrationDto): ResponseEntity<String> {
        logger.info("Registration request received: $dto")
        return runBlocking { registrationClient.register(dto) }
    }
}
