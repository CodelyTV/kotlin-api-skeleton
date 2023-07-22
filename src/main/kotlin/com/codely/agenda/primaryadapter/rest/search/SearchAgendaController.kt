package com.codely.agenda.primaryadapter.rest.search

import arrow.core.raise.fold
import com.codely.agenda.application.search.SearchAgendaError
import com.codely.agenda.application.search.SearchAgendaError.Unknown
import com.codely.agenda.application.search.SearchAgendasQuery
import com.codely.agenda.application.search.handle
import com.codely.agenda.domain.AgendaRepository
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchAgendaController(private val repository: AgendaRepository) {

    @GetMapping("/agendas")
    @CrossOrigin(allowCredentials = "true", originPatterns = ["*"], allowedHeaders = ["*"], methods = [RequestMethod.GET, RequestMethod.OPTIONS])
    fun search(@RequestParam week: Int, @RequestParam year: Int): ResponseEntity<*> = runBlocking {
        with(repository) {
            fold(
                block = { handle(SearchAgendasQuery(week, year)) },
                recover = { error -> error.toServerError() },
                transform = { agendas -> ResponseEntity.status(HttpStatus.OK).body(agendas) }
            )
        }
    }

    private fun SearchAgendaError.toServerError(): ResponseEntity<*> =
        when (this) {
            is Unknown -> throw cause
        }
}
