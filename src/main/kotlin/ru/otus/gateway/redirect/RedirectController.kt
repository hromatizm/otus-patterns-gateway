package ru.otus.gateway.redirect

import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.otus.gateway.util.lazyLogger

@RestController
@RequestMapping("/api/v1/")
class RedirectController(
    private val redirectClient: RedirectClient
) {

    private val logger by lazyLogger()

    @GetMapping("/smart-link/{uri}")
    fun get(
        @PathVariable uri: String,
        @RequestHeader headers: Map<String, String>
    ): ResponseEntity<Any> {
        logger.info("Smart-link request received: $uri")
        return runBlocking {
            redirectClient.getLink(uri = uri, args = headers)
        }
    }

}
