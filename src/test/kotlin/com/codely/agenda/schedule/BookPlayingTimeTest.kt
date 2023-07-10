package com.codely.agenda.schedule

import com.codely.agenda.domain.PlayerName
import com.codely.agenda.fakes.FakeAgendaRepository
import com.codely.agenda.mothers.AgendaMother
import com.codely.agenda.primaryadapter.rest.book.BookAgendaController
import com.codely.agenda.primaryadapter.rest.book.BookAgendaDTO
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
class BookPlayingTimeTest {

    private val repository = FakeAgendaRepository()

    private val controller = BookAgendaController(repository)

    @BeforeEach
    fun setUp() {
        repository.resetFake()
    }

    @Test
    fun `should add a player to an available hour if max capacity has not reached`() = runTest {
        // Given
        repository.save(agenda)

        // When
        val result = controller.bookAgenda(agenda.id.toString(), requestBody)

        // Then
        assertEquals(HttpStatus.OK, result.statusCode)
        assertTrue { repository.containsResource(expectedAgenda) }
    }


    @Test
    fun `should not add a player for an hour if it's reached max capacity`() = runTest {
        TODO("Not yet implemented")
    }

    @Test
    fun `should not add a player to an available hour twice`() = runTest {
        TODO("Not yet implemented")
    }

    private val agenda = AgendaMother.tuesday()
    private val fullAgenda = AgendaMother.fullyBooked()

    private val expectedAgenda = agenda.addPlayer(agenda.availableHours.first().id, PlayerName("Rafa"))
    private val requestBody = BookAgendaDTO("Rafa", agenda.availableHours.first().id)
}