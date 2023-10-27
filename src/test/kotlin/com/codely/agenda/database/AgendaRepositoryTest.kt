package com.codely.agenda.database

import com.codely.agenda.AgendaMother
import com.codely.agenda.secondaryadapter.database.MongoAgendaRepository
import com.codely.agenda.secondaryadapter.database.document.JpaAgendaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest

@ExperimentalCoroutinesApi
@DataMongoTest
class AgendaRepositoryTest(@Autowired private val jpaRepository: JpaAgendaRepository) {

    private val repository = MongoAgendaRepository(jpaRepository)

//    @Disabled
//    @Test
//    fun `should find an existing agenda`() = runTest {
//        // Given
//        repository.save(agenda)
//
//        // When
//        repository.findBy(Id(agenda.id))
//            .shouldBeRight(agenda)
//    }

    private val agenda = AgendaMother.fullyBooked()
}
