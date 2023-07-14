package com.codely.agenda.book

import arrow.core.getOrElse
import com.codely.agenda.AgendaMother
import com.codely.agenda.domain.Player
import com.codely.agenda.fakes.FakeAgendaRepository
import com.codely.agenda.primaryadapter.rest.book.BookAgendaController
import com.codely.agenda.primaryadapter.rest.book.BookAgendaDTO
import com.codely.shared.error.ServerError
import com.codely.shared.error.UserServerErrors.AGENDA_DOES_NOT_EXIST
import com.codely.shared.error.UserServerErrors.AVAILABLE_HOUR_DOES_NOT_EXIST
import com.codely.shared.error.UserServerErrors.MAX_CAPACITY_REACHED
import com.codely.shared.error.UserServerErrors.USER_ALREADY_BOOKED
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK

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
        val result = controller.bookAgenda(agenda.id.toString(), hourId.toString(), requestBody)

        // Then
        assertEquals(OK, result.statusCode)
        assertTrue { repository.containsResource(expectedAgenda) }
    }

    @Test
    fun `should not add a player for an hour if it's reached max capacity`() = runTest {
        // Given
        repository.save(fullAgenda)

        // When
        val result = controller.bookAgenda(fullAgenda.id.toString(), fullAgendaHourdId.toString(), fullAgendaRequestBody)

        // Then
        assertEquals(CONFLICT, result.statusCode)
        assertEquals(ServerError.of(MAX_CAPACITY_REACHED), result.body)
    }

    @Test
    fun `should not add a player to an available hour twice`() = runTest {
        // Given
        val updatedAgenda = agenda
            .bookAvailableHour(hourId, Player("Rafa"))
            .getOrElse { agenda }

        repository.save(updatedAgenda)

        // When
        val result = controller.bookAgenda(agenda.id.toString(), hourId.toString(), requestBody)

        // Then
        assertEquals(CONFLICT, result.statusCode)
        assertEquals(ServerError.of(USER_ALREADY_BOOKED), result.body)
    }

    @Test
    fun `should not add a player to consecutive available hours`() = runTest {
        // Given
        val updatedAgenda = agenda
            .bookAvailableHour(agenda.availableHours.last().id, Player("Rafa"))
            .getOrElse { agenda }

        repository.save(updatedAgenda)

        // When
        val result = controller.bookAgenda(agenda.id.toString(), hourId.toString(), requestBody)

        // Then
        assertEquals(CONFLICT, result.statusCode)
        assertEquals(ServerError.of(USER_ALREADY_BOOKED), result.body)
    }

    @Test
    fun `should fail if agenda does not exist`() = runTest {
        // When
        val result = controller.bookAgenda(agenda.id.toString(), hourId.toString(), requestBody)

        // Then
        assertEquals(NOT_FOUND, result.statusCode)
        assertEquals(ServerError.of(AGENDA_DOES_NOT_EXIST), result.body)
    }

    @Test
    fun `should fail if available hour does not exist`() = runTest {
        // Given
        val updatedAgenda = agenda.copy(availableHours = emptyList())
        repository.save(updatedAgenda)

        // When
        val result = controller.bookAgenda(agenda.id.toString(), hourId.toString(), requestBody)

        // Then
        assertEquals(NOT_FOUND, result.statusCode)
        assertEquals(ServerError.of(AVAILABLE_HOUR_DOES_NOT_EXIST), result.body)
    }

    private val agenda = AgendaMother.tuesday()
    private val fullAgenda = AgendaMother.fullyBooked()

    private val expectedAgenda = agenda
        .bookAvailableHour(agenda.availableHours.first().id, Player("Rafa"))
        .getOrElse { agenda }

    private val hourId = agenda.availableHours.first().id
    private val fullAgendaHourdId = fullAgenda.availableHours.first().id

    private val requestBody = BookAgendaDTO("Rafa")

    private val fullAgendaRequestBody = BookAgendaDTO("Rafa",)
}
