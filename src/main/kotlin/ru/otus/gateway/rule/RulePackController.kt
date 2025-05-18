package ru.otus.gateway.rule

import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.otus.gateway.util.lazyLogger

@RestController
@RequestMapping("/api/v1/rule-pack")
class RulePackController(
    private val rulePackClient: RulePackClient
) {

    private val logger by lazyLogger()

    @PostMapping("/create")
    fun create(@RequestBody dto: RulePackDto): ResponseEntity<Any> {
        logger.info("Create rule-pack request received: $dto")
        return runBlocking { rulePackClient.create(dto) }
    }

    @GetMapping("/get")
    fun get(@RequestParam uri: String): ResponseEntity<Any> {
        logger.info("Get rule-pack request received: $uri")
        return runBlocking { rulePackClient.get(uri) }
    }

    @PutMapping("/update")
    fun update(@RequestBody dto: RulePackDto): ResponseEntity<Any> {
        logger.info("Update rule-pack request received: $dto")
        return runBlocking { rulePackClient.update(dto) }
    }

    @DeleteMapping("/delete")
    fun delete(@RequestParam uri: String): ResponseEntity<Any> {
        logger.info("Delete rule-pack request received: $uri")
        return runBlocking { rulePackClient.delete(uri) }
    }

}