package ru.otus.gateway.rule.update

import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.otus.gateway.rule.RulePackDto
import ru.otus.gateway.util.lazyLogger

@RestController
@RequestMapping("/api/v1/rule-pack")
class UpdateRulePackController(
    private val updateRulePackClient: UpdateRulePackClient
) {

    private val logger by lazyLogger()

    @PutMapping("/update")
    fun update(@RequestBody dto: RulePackDto): ResponseEntity<Any> {
        logger.info("Update rule-pack request received: $dto")
        return runBlocking { updateRulePackClient.update(dto) }
    }

}
