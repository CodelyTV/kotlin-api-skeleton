package com.codely.agenda.secondaryadapter.database

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.raise.Raise
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaFindByCriteria
import com.codely.agenda.domain.AgendaFindByCriteria.Id
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.secondaryadapter.database.document.JpaAgendaRepository
import com.codely.agenda.secondaryadapter.database.document.toDocument
import org.springframework.stereotype.Component

@Component
class MongoAgendaRepository(private val repository: JpaAgendaRepository) : AgendaRepository {

    override suspend fun save(agenda: Agenda): Either<Throwable, Unit> = catch { repository.save(agenda.toDocument()) }
    override suspend fun findBy(criteria: AgendaFindByCriteria): Either<Throwable, Agenda> = catch {
        when (criteria) {
            is Id -> repository.findById(criteria.id.toString()).get().toAgenda()
        }
    }

    context(Raise<Throwable>) override suspend fun findByDsl(criteria: AgendaFindByCriteria): Agenda {
        TODO("Not yet implemented")
    }
}
