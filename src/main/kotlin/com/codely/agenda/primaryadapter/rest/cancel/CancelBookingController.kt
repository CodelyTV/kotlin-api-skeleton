package com.codely.agenda.primaryadapter.rest.cancel

import arrow.core.raise.fold
import com.codely.agenda.application.cancel.CancelBookingCommand
import com.codely.agenda.application.cancel.CancelBookingError
import com.codely.agenda.application.cancel.CancelBookingError.AgendaNotFound
import com.codely.agenda.application.cancel.CancelBookingError.AvailableHourNotFound
import com.codely.agenda.application.cancel.CancelBookingError.InvalidUUID
import com.codely.agenda.application.cancel.CancelBookingError.PlayerNotBooked
import com.codely.agenda.application.cancel.CancelBookingError.Unknown
import com.codely.agenda.application.cancel.handle
import com.codely.agenda.domain.AgendaRepository
import com.codely.shared.error.ServerError
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.AGENDA_DOES_NOT_EXIST
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.AVAILABLE_HOUR_DOES_NOT_EXIST
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.INVALID_IDENTIFIERS
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.USER_NOT_BOOKED
import com.codely.shared.cors.BaseController
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CancelBookingController(private val repository: AgendaRepository) : BaseController() {

    @DeleteMapping("/agendas/{agendaId}/hours/{hourId}")
    fun cancel(@PathVariable agendaId: String, @PathVariable hourId: String, @RequestBody body: CancelBookingDTO): ResponseEntity<*> = runBlocking {
        with(repository) {
            fold(
                block = { handle(CancelBookingCommand(id = agendaId, hourId = hourId, playerName = body.playerName)) },
                recover = { error -> error.toServerError() },
                transform = { agenda -> ResponseEntity.status(OK).body(agenda) }
            )
        }
    }

    private fun CancelBookingError.toServerError(): ResponseEntity<*> =
        when (this) {
            is AgendaNotFound -> ResponseEntity.status(NOT_FOUND).body(ServerError.of(AGENDA_DOES_NOT_EXIST))
            is AvailableHourNotFound -> ResponseEntity.status(NOT_FOUND).body(ServerError.of(AVAILABLE_HOUR_DOES_NOT_EXIST))
            is PlayerNotBooked -> ResponseEntity.status(NOT_FOUND).body(ServerError.of(USER_NOT_BOOKED))
            is Unknown -> throw cause
            is InvalidUUID -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ServerError.of(INVALID_IDENTIFIERS))
        }
}
