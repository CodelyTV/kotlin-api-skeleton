package com.codely.agenda.primaryadapter.rest.book

import arrow.core.raise.fold
import com.codely.agenda.application.book.BookAgendaCommand
import com.codely.agenda.application.book.BookAgendaError
import com.codely.agenda.application.book.BookAgendaError.AgendaNotFound
import com.codely.agenda.application.book.BookAgendaError.AvailableHourNotFound
import com.codely.agenda.application.book.BookAgendaError.MaxCapacityReached
import com.codely.agenda.application.book.BookAgendaError.PlayerAlreadyBooked
import com.codely.agenda.application.book.BookAgendaError.Unknown
import com.codely.agenda.application.book.BookAgendaError.InvalidUUID
import com.codely.agenda.application.book.handle
import com.codely.agenda.domain.AgendaRepository
import com.codely.shared.error.ServerError
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.AGENDA_DOES_NOT_EXIST
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.AVAILABLE_HOUR_DOES_NOT_EXIST
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.INVALID_IDENTIFIERS
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.MAX_CAPACITY_REACHED
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.USER_ALREADY_BOOKED
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class BookAgendaController(private val repository: AgendaRepository) {

    @PostMapping("/agendas/{agendaId}/hours/{hourId}")
    fun bookAgenda(@PathVariable agendaId: String, @PathVariable hourId: String, @RequestBody body: BookAgendaDTO): ResponseEntity<*> = runBlocking {
        with(repository) {
            fold(
                block = { handle(BookAgendaCommand(id = agendaId, hourId = hourId, playerName = body.playerName)) },
                recover = { error -> error.toServerError() },
                transform = { agenda -> ResponseEntity.status(OK).body(agenda) }
            )
        }
    }

    private fun BookAgendaError.toServerError() =
        when (this) {
            is MaxCapacityReached -> ResponseEntity.status(CONFLICT).body(ServerError.of(MAX_CAPACITY_REACHED))
            is PlayerAlreadyBooked -> ResponseEntity.status(CONFLICT).body(ServerError.of(USER_ALREADY_BOOKED))
            is AgendaNotFound -> ResponseEntity.status(NOT_FOUND).body(ServerError.of(AGENDA_DOES_NOT_EXIST))
            is AvailableHourNotFound -> ResponseEntity.status(NOT_FOUND).body(ServerError.of(AVAILABLE_HOUR_DOES_NOT_EXIST))
            is Unknown -> throw cause
            is InvalidUUID -> ResponseEntity.status(BAD_REQUEST).body(ServerError.of(INVALID_IDENTIFIERS))
        }
}
