package com.codely.agenda.secondaryadapter.database

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaFindByCriteria
import com.codely.agenda.domain.AgendaFindByCriteria.Id
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.AgendaSearchByCriteria
import com.codely.agenda.domain.AgendaSearchByCriteria.WeekAndYear
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

    override suspend fun searchBy(criteria: AgendaSearchByCriteria): Either<Throwable, List<Agenda>> = catch {
        when (criteria) {
            is WeekAndYear -> repository.findAllByWeekAndYear(criteria.week, criteria.year).map { document -> document.toAgenda() }
        }
    }
}
