package com.codely.agenda.schedule

import arrow.core.getOrElse
import com.codely.agenda.domain.PlayerName
import com.codely.agenda.fakes.FakeAgendaRepository
import com.codely.agenda.mothers.AgendaMother
import com.codely.agenda.primaryadapter.rest.book.BookAgendaController
import com.codely.agenda.primaryadapter.rest.book.BookAgendaDTO
import com.codely.shared.error.ServerError
import com.codely.shared.error.UserServerErrors
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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
        // Given
        repository.save(fullAgenda)

        // When
        val result = controller.bookAgenda(fullAgenda.id.toString(), fullAgendaRequestBody)

        // Then
        assertEquals(HttpStatus.CONFLICT, result.statusCode)
        assertEquals(ServerError.of(UserServerErrors.MAX_CAPACITY_REACHED), result.body)
    }

    @Test
    fun `should not add a player to an available hour twice`() = runTest {
        // Given
        val updatedAgenda = agenda
            .bookAvailableHour(agenda.availableHours.first().id, PlayerName("Rafa"))
            .getOrElse { agenda }

        repository.save(updatedAgenda)

        // When
        val result = controller.bookAgenda(agenda.id.toString(), requestBody)

        // Then
        assertEquals(HttpStatus.CONFLICT, result.statusCode)
        assertEquals(ServerError.of(UserServerErrors.USER_ALREADY_BOOKED), result.body)
    }

    private val agenda = AgendaMother.tuesday()
    private val fullAgenda = AgendaMother.fullyBooked()

    private val expectedAgenda = agenda
        .bookAvailableHour(agenda.availableHours.first().id, PlayerName("Rafa"))
        .getOrElse { agenda }
    private val requestBody = BookAgendaDTO("Rafa", agenda.availableHours.first().id)

    private val fullAgendaRequestBody = BookAgendaDTO("Rafa", fullAgenda.availableHours.first().id)
}