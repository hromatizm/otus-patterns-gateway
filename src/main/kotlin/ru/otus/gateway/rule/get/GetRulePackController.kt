package ru.otus.gateway.rule.get

import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.otus.gateway.rule.RulePackDto
import ru.otus.gateway.util.lazyLogger

@RestController
@RequestMapping("/api/v1/rule-pack")
class GetRulePackController(
    private val getRulePackClient: GetRulePackClient
) {

    private val logger by lazyLogger()

    @GetMapping("/get")
    fun get(@RequestParam uri: String): ResponseEntity<Any> {
        logger.info("Get rule-pack request received: $uri")
        return runBlocking { getRulePackClient.get(uri) }
    }

}