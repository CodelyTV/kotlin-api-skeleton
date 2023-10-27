package com.codely.agenda.cancel

import arrow.core.raise.recover
import com.codely.agenda.AgendaMother
import com.codely.agenda.domain.Player
import com.codely.agenda.fakes.FakeAgendaRepository
import com.codely.agenda.primaryadapter.rest.cancel.CancelBookingController
import com.codely.agenda.primaryadapter.rest.cancel.CancelBookingDTO
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.USER_NOT_BOOKED
import com.codely.shared.error.ServerError
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK

@ExperimentalCoroutinesApi
class CancelBookingTest {

    private val repository = FakeAgendaRepository()

    private val controller = CancelBookingController(repository)

    @BeforeEach
    fun setUp() {
        repository.resetFake()
    }

    @Test
    fun `should remove a player from an available hour`() = runTest {
        // Given
        repository.save(fullAgenda)

        // When
        val result = controller.cancel(fullAgenda.id.toString(), fullAgendaHourId.toString(), fullAgendaRequestBody)

        // Then
        assertEquals(OK, result.statusCode)
        assertTrue { repository.containsResource(expectedAgenda) }
    }

    @Test
    fun `should not remove a player from an hour if it doesnt have a booking`() = runTest {
        // Given
        repository.save(emptyAgenda)

        // When
        val result = controller.cancel(emptyAgenda.id.toString(), emptyAgendaHourId.toString(), requestBody)

        // Then
        assertEquals(NOT_FOUND, result.statusCode)
        assertEquals(ServerError.of(USER_NOT_BOOKED), result.body)
    }

    private val player = Player("Exposito")
    private val fullAgenda = AgendaMother.fullyBooked()
    private val expectedAgenda =
        recover(
            block = { fullAgenda.cancelBooking(fullAgenda.availableHours.first().id, player) },
            recover = { fullAgenda }
        )

    private val fullAgendaHourId = fullAgenda.availableHours.first().id

    private val fullAgendaRequestBody = CancelBookingDTO(player.name)

    private val emptyAgenda = AgendaMother.tuesday()
    private val emptyAgendaHourId = emptyAgenda.availableHours.first().id
    private val requestBody = CancelBookingDTO(player.name)
}
