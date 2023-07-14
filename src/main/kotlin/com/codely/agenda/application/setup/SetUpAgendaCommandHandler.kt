package com.codely.agenda.application.setup

import arrow.core.raise.Raise
import com.codely.agenda.domain.AgendaRepository

context(AgendaRepository, Raise<ConfigureAgendaError>)
suspend fun handle(command: SetUpAgendaCommand) = configureAgenda(command.year)

data class SetUpAgendaCommand(val year: Int)
