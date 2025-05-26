package ru.otus.gateway.rule.delete

import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.otus.gateway.util.lazyLogger

@RestController
@RequestMapping("/api/v1/rule-pack")
class DeleteRulePackController(
    private val deleteRulePackClient: DeleteRulePackClient
) {

    private val logger by lazyLogger()

    @DeleteMapping("/delete")
    fun delete(@RequestParam uri: String): ResponseEntity<Any> {
        logger.info("Delete rule-pack request received: $uri")
        return runBlocking { deleteRulePackClient.delete(uri) }
    }

}
