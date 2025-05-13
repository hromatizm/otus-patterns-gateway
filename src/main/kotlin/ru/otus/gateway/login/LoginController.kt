package ru.otus.gateway.login

import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import util.lazyLogger

@RestController
@RequestMapping("/api/v1")
class LoginController(
    private val loginClient: LoginClient,
) {

    private val logger by lazyLogger()

    @PostMapping("/login")
    fun register(@RequestBody dto: LoginDto): ResponseEntity<String> {
        logger.info("Login request received: $dto")
        return runBlocking { loginClient.login(dto) }
    }
}