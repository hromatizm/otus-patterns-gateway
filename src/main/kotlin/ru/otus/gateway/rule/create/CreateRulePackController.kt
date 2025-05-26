package ru.otus.gateway.rule.create

import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.otus.gateway.rule.RulePackDto
import ru.otus.gateway.util.lazyLogger

@RestController
@RequestMapping("/api/v1/rule-pack")
class CreateRulePackController(
    private val createRulePackClient: CreateRulePackClient
) {

    private val logger by lazyLogger()

    @PostMapping("/create")
    fun create(@RequestBody dto: RulePackDto): ResponseEntity<Any> {
        logger.info("Create rule-pack request received: $dto")
        return runBlocking { createRulePackClient.create(dto) }
    }

}