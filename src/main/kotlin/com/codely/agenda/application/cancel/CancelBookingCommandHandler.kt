package com.codely.agenda.application.cancel

import arrow.core.Either
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.Player
import java.util.UUID

context(AgendaRepository)
suspend fun handle(command: CancelBookingCommand): Either<CancelBookingError, Agenda> =
    cancelBooking(id = command.id, name = Player(value = command.playerName), hourId = command.hourId)

data class CancelBookingCommand(val id: UUID, val hourId: UUID, val playerName: String)