package com.codely.agenda.primaryadapter.rest.disable

import arrow.core.raise.fold
import com.codely.admin.domain.AdminRepository
import com.codely.admin.primaryadapter.rest.error.AdminServerErrors.INVALID_ACCESS_KEY
import com.codely.agenda.application.disable.DisableAgendaCommand
import com.codely.agenda.application.disable.DisableAgendaError
import com.codely.agenda.application.disable.DisableAgendaError.AgendaNotFound
import com.codely.agenda.application.disable.DisableAgendaError.ForbiddenAction
import com.codely.agenda.application.disable.DisableAgendaError.InvalidUUID
import com.codely.agenda.application.disable.DisableAgendaError.Unknown
import com.codely.agenda.application.disable.handle
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.AGENDA_DOES_NOT_EXIST
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.INVALID_IDENTIFIERS
import com.codely.shared.authorization.executeIfAllowed
import com.codely.shared.cors.BaseController
import com.codely.shared.error.ServerError
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class DisableAgendaController(
    private val repository: AgendaRepository,
    private val adminRepository: AdminRepository
) : BaseController() {

    @PatchMapping("/agendas/{agendaId}/disable")
    fun disableAgenda(@PathVariable agendaId: String, @RequestParam accessKey: String): ResponseEntity<*> = runBlocking {
        with(repository) {
            with(adminRepository) {
                fold(
                    block = {
                        executeIfAllowed(
                            accessKey,
                            block = { handle(DisableAgendaCommand(agendaId)) },
                            recover = { ForbiddenAction }
                        )
                    },
                    recover = { error -> error.toServerError() },
                    transform = { agenda -> ResponseEntity.status(OK).body(agenda) }
                )
            }
        }
    }

    private fun DisableAgendaError.toServerError() =
        when (this) {
            is AgendaNotFound -> ResponseEntity.status(NOT_FOUND).body(ServerError.of(AGENDA_DOES_NOT_EXIST))
            is InvalidUUID -> ResponseEntity.status(BAD_REQUEST).body(ServerError.of(INVALID_IDENTIFIERS))
            is ForbiddenAction -> ResponseEntity.status(FORBIDDEN).body(ServerError.of(INVALID_ACCESS_KEY))
            is Unknown -> throw cause
        }
}
