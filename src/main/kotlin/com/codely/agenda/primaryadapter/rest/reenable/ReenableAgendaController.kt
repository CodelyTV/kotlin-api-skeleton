package com.codely.agenda.primaryadapter.rest.reenable

import arrow.core.raise.fold
import com.codely.admin.domain.AdminRepository
import com.codely.admin.primaryadapter.rest.error.AdminServerErrors.INVALID_ACCESS_KEY
import com.codely.agenda.application.reenable.ReenableAgendaCommand
import com.codely.agenda.application.reenable.ReenableAgendaError
import com.codely.agenda.application.reenable.ReenableAgendaError.AgendaNotFound
import com.codely.agenda.application.reenable.ReenableAgendaError.ForbiddenAction
import com.codely.agenda.application.reenable.ReenableAgendaError.InvalidUUID
import com.codely.agenda.application.reenable.ReenableAgendaError.Unknown
import com.codely.agenda.application.reenable.handle
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.AGENDA_DOES_NOT_EXIST
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.INVALID_IDENTIFIERS
import com.codely.shared.authorization.executeIfAllowed
import com.codely.shared.error.ServerError
import com.codely.shared.response.Response
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ReenableAgendaController(
    private val repository: AgendaRepository,
    private val adminRepository: AdminRepository
) {

    @CrossOrigin
    @PatchMapping("/agendas/{agendaId}/reenable")
    fun reenableAgenda(@PathVariable agendaId: String, @RequestParam accessKey: String): ResponseEntity<*> = runBlocking {
        with(repository) {
            with(adminRepository) {
                fold(
                    block = {
                        executeIfAllowed(
                            accessKey,
                            block = { handle(ReenableAgendaCommand(agendaId)) },
                            recover = { ForbiddenAction }
                        )
                    },
                    recover = { error -> error.toServerError() },
                    transform = { agenda -> ResponseEntity.status(OK).body(agenda) }
                )
            }
        }
    }

    private fun ReenableAgendaError.toServerError() =
        when (this) {
            is AgendaNotFound -> Response.status(NOT_FOUND).body(ServerError.of(AGENDA_DOES_NOT_EXIST))
            is InvalidUUID -> Response.status(BAD_REQUEST).body(ServerError.of(INVALID_IDENTIFIERS))
            is ForbiddenAction -> Response.status(FORBIDDEN).body(ServerError.of(INVALID_ACCESS_KEY))
            is Unknown -> throw cause
        }
}
