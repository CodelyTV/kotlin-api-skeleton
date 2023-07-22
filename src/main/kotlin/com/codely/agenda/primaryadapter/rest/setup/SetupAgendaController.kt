package com.codely.agenda.primaryadapter.rest.setup

import arrow.core.raise.fold
import com.codely.agenda.application.setup.ConfigureAgendaError
import com.codely.agenda.application.setup.ConfigureAgendaError.Unknown
import com.codely.agenda.application.setup.SetUpAgendaCommand
import com.codely.agenda.application.setup.handle
import com.codely.agenda.domain.AgendaRepository
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SetupAgendaController(private val repository: AgendaRepository) {

    @PostMapping("/agenda/setUp/{year}")
    fun setUp(@PathVariable year: String): ResponseEntity<*> = runBlocking {
      with(repository) {
          fold(
              block = { handle(SetUpAgendaCommand(year.toInt())) },
              recover = { error -> error.toServerError() },
              transform = { ResponseEntity.status(HttpStatus.ACCEPTED).body(null) }
          )
      }
    }

    private fun ConfigureAgendaError.toServerError(): ResponseEntity<*> =
        when (this) {
            is Unknown -> throw cause
        }
}
