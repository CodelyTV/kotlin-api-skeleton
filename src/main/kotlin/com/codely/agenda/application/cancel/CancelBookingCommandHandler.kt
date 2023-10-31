package com.codely.agenda.application.cancel

import arrow.core.raise.Raise
import arrow.core.raise.catch
import com.codely.agenda.application.cancel.CancelBookingError.InvalidPlayerName
import com.codely.agenda.application.cancel.CancelBookingError.InvalidUUID
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.Player
import java.util.UUID

context(AgendaRepository, Raise<CancelBookingError>)
suspend fun handle(command: CancelBookingCommand): Agenda {
    val id = catch(block = { UUID.fromString(command.id) }) { raise(InvalidUUID) }
    val hourId = catch(block = { UUID.fromString(command.hourId) }) { raise(InvalidUUID) }
    val player = Player.createOrElse(name = command.playerName) { InvalidPlayerName }

    return cancelBooking(id = id, name = player, hourId = hourId)
}

data class CancelBookingCommand(val id: String, val hourId: String, val playerName: String)
