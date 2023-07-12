package com.codely.agenda.primaryadapter.rest.book

import com.codely.agenda.application.book.BookAgendaCommand
import com.codely.agenda.application.book.BookAgendaError
import com.codely.agenda.application.book.BookAgendaError.AgendaNotFound
import com.codely.agenda.application.book.BookAgendaError.AvailableHourNotFound
import com.codely.agenda.application.book.BookAgendaError.MaxCapacityReached
import com.codely.agenda.application.book.BookAgendaError.PlayerAlreadyBooked
import com.codely.agenda.application.book.BookAgendaError.Unknown
import com.codely.agenda.application.book.handle
import com.codely.agenda.domain.AgendaRepository
import com.codely.shared.error.ServerError
import com.codely.shared.error.UserServerErrors.AGENDA_DOES_NOT_EXIST
import com.codely.shared.error.UserServerErrors.AVAILABLE_HOUR_DOES_NOT_EXIST
import com.codely.shared.error.UserServerErrors.MAX_CAPACITY_REACHED
import com.codely.shared.error.UserServerErrors.USER_ALREADY_BOOKED
import com.codely.shared.response.toServerResponse
import java.util.UUID
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class BookAgendaController(private val repository: AgendaRepository) {

    @PostMapping("/agenda/{id}/book")
    suspend fun bookAgenda(@PathVariable id: String, @RequestBody body: BookAgendaDTO): ResponseEntity<*> = runBlocking {
        with(repository) {
            handle(BookAgendaCommand(id = UUID.fromString(id), hourId = body.availableHourId, playerName = body.playerName))
                .toServerResponse(
                    onValidResponse = { agenda -> ResponseEntity.status(HttpStatus.OK).body(agenda) },
                    onError = { error -> error.toServerError() }
                )
        }
    }

    private fun BookAgendaError.toServerError(): ResponseEntity<*> =
        when (this) {
            is MaxCapacityReached -> ResponseEntity.status(HttpStatus.CONFLICT).body(ServerError.of(MAX_CAPACITY_REACHED))
            is PlayerAlreadyBooked -> ResponseEntity.status(HttpStatus.CONFLICT).body(ServerError.of(USER_ALREADY_BOOKED))
            is AgendaNotFound -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ServerError.of(AGENDA_DOES_NOT_EXIST))
            is AvailableHourNotFound -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ServerError.of(
                AVAILABLE_HOUR_DOES_NOT_EXIST
            ))
            is Unknown -> throw cause
        }
}
