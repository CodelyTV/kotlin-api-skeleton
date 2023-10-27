package com.codely.agenda.primaryadapter.rest.search

import com.codely.agenda.application.search.SearchAgendasQuery
import com.codely.agenda.application.search.handle
import com.codely.agenda.domain.AgendaRepository
import com.codely.shared.cors.BaseController
import com.codely.shared.response.Response
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchAgendaController(private val repository: AgendaRepository) : BaseController() {

    @GetMapping("/agendas")
    fun search(@RequestParam week: Int, @RequestParam year: Int): Response<*> = runBlocking {
        with(repository) {
            handle(SearchAgendasQuery(week, year))
                .let { agendas -> Response.status(OK).body(agendas) }
        }
    }
}
