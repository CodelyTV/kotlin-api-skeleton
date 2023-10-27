package com.codely.agenda.primaryadapter.rest.disable

import arrow.core.raise.fold
import com.codely.admin.domain.AdminRepository
import com.codely.admin.primaryadapter.rest.error.AdminServerErrors.INVALID_ACCESS_KEY
import com.codely.agenda.application.disable.DisableAgendaCommand
import com.codely.agenda.application.disable.DisableAgendaError
import com.codely.agenda.application.disable.DisableAgendaError.AgendaNotFound
import com.codely.agenda.application.disable.DisableAgendaError.ForbiddenAction
import com.codely.agenda.application.disable.DisableAgendaError.InvalidUUID
import com.codely.agenda.application.disable.handle
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.AGENDA_DOES_NOT_EXIST
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.INVALID_IDENTIFIERS
import com.codely.shared.authorization.executeIfAllowed
import com.codely.shared.cors.BaseController
import com.codely.shared.response.Response
import com.codely.shared.response.withBody
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
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
    fun disableAgenda(@PathVariable agendaId: String, @RequestParam accessKey: String): Response<*> = runBlocking {
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
                    transform = { agenda -> Response.status(OK).body(agenda) }
                )
            }
        }
    }

    private fun DisableAgendaError.toServerError() =
        when (this) {
            is AgendaNotFound -> Response.status(NOT_FOUND).withBody(AGENDA_DOES_NOT_EXIST)
            is InvalidUUID -> Response.status(BAD_REQUEST).withBody(INVALID_IDENTIFIERS)
            is ForbiddenAction -> Response.status(FORBIDDEN).withBody(INVALID_ACCESS_KEY)
        }
}
