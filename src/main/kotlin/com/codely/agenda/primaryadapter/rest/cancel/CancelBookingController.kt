package com.codely.agenda.primaryadapter.rest.cancel

import com.codely.agenda.application.cancel.CancelBookingCommand
import com.codely.agenda.application.cancel.CancelBookingError
import com.codely.agenda.application.cancel.CancelBookingError.AgendaNotFound
import com.codely.agenda.application.cancel.CancelBookingError.AvailableHourNotFound
import com.codely.agenda.application.cancel.CancelBookingError.PlayerNotBooked
import com.codely.agenda.application.cancel.CancelBookingError.Unknown
import com.codely.agenda.application.cancel.handle
import com.codely.agenda.domain.AgendaRepository
import com.codely.shared.error.ServerError
import com.codely.shared.error.UserServerErrors.AGENDA_DOES_NOT_EXIST
import com.codely.shared.error.UserServerErrors.AVAILABLE_HOUR_DOES_NOT_EXIST
import com.codely.shared.error.UserServerErrors.USER_NOT_BOOKED
import com.codely.shared.response.toServerResponse
import java.util.UUID
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CancelBookingController(private val repository: AgendaRepository) {

    @DeleteMapping("/agenda/{id}/book")
    suspend fun cancel(@PathVariable id: String, @RequestBody body: CancelBookingDTO): ResponseEntity<*> = runBlocking {
        with(repository) {
            handle(CancelBookingCommand(id = UUID.fromString(id), hourId = body.availableHourId, playerName = body.playerName))
                .toServerResponse(
                    onValidResponse = { agenda -> ResponseEntity.status(OK).body(agenda) },
                    onError = { error -> error.toServerError() }
                )
        }
    }

    private fun CancelBookingError.toServerError(): ResponseEntity<*> =
        when (this) {
            is AgendaNotFound -> ResponseEntity.status(NOT_FOUND).body(ServerError.of(AGENDA_DOES_NOT_EXIST))
            is AvailableHourNotFound -> ResponseEntity.status(NOT_FOUND).body(ServerError.of(AVAILABLE_HOUR_DOES_NOT_EXIST))
            is PlayerNotBooked -> ResponseEntity.status(NOT_FOUND).body(ServerError.of(USER_NOT_BOOKED))
            is Unknown -> throw cause
        }
}
