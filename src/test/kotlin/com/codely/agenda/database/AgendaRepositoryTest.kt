package com.codely.agenda.database

import com.codely.agenda.AgendaMother
import com.codely.agenda.domain.AgendaFindByCriteria.Id
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.secondaryadapter.database.MongoAgendaRepository
import com.codely.agenda.secondaryadapter.database.document.JpaAgendaRepository
import io.kotest.assertions.arrow.core.shouldBeRight
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers


@ExperimentalCoroutinesApi
@Testcontainers
@ContextConfiguration(classes = [MongoContainerConfig::class])
class AgendaRepositoryTest(@Autowired private val jpaRepository: JpaAgendaRepository) {

    private val repository = MongoAgendaRepository(jpaRepository)

    @Test
    fun `should find an existing agenda`() = runTest {
        // Given
        repository.save(agenda)
            .shouldBeRight()

        // When
        repository.findBy(Id(agenda.id))
            .shouldBeRight(agenda)
    }

    private val agenda = AgendaMother.fullyBooked()
}
