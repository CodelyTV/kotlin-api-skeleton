package com.codely.agenda.secondaryadapter.database

import arrow.core.Either
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaFindByCriteria
import com.codely.agenda.domain.AgendaRepository
import org.springframework.stereotype.Component

@Component
class MongoAgendaRepository : AgendaRepository {
    override suspend fun save(agenda: Agenda): Either<Throwable, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun findBy(criteria: AgendaFindByCriteria): Either<Throwable, Agenda> {
        TODO("Not yet implemented")
    }
}
