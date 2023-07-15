package com.codely.agenda.reenable

import com.codely.admin.AdminMother
import com.codely.admin.fakes.FakeAdminRepository
import com.codely.admin.primaryadapter.rest.error.AdminServerErrors.INVALID_ACCESS_KEY
import com.codely.agenda.AgendaMother
import com.codely.agenda.domain.AvailableHour
import com.codely.agenda.fakes.FakeAgendaRepository
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.AGENDA_DOES_NOT_EXIST
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.INVALID_IDENTIFIERS
import com.codely.agenda.primaryadapter.rest.reenable.ReenableAgendaController
import com.codely.shared.error.ServerError
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK

@ExperimentalCoroutinesApi
class ReenableAgendaTest {

    private val repository = FakeAgendaRepository()
    private val adminRepository = FakeAdminRepository()

    private val controller = ReenableAgendaController(repository, adminRepository)

    @BeforeEach
    fun setUp() {
        repository.resetFake()
        adminRepository.resetFake()
    }

    @Test
    fun `should add all available hours for the given agenda`() = runTest {
        // Given
        repository.save(agenda)
        adminRepository.save(admin)

        // When
        val result = controller.reenableAgenda(agenda.id.toString(), admin.key.value)

        // Then
        assertEquals(OK, result.statusCode)
    }

    @Test
    fun `should fail if agenda does not exist`() = runTest {
        // Given
        adminRepository.save(admin)

        // When
        val result = controller.reenableAgenda(agenda.id.toString(), admin.key.value)

        // Then
        assertEquals(NOT_FOUND, result.statusCode)
        assertEquals(ServerError.of(AGENDA_DOES_NOT_EXIST), result.body)
    }

    @Test
    fun `should fail if agenda id is not a UUID`() = runTest {
        // Given
        repository.save(agenda)
        adminRepository.save(admin)

        // When
        val result = controller.reenableAgenda("1234532", admin.key.value)

        // Then
        assertEquals(BAD_REQUEST, result.statusCode)
        assertEquals(ServerError.of(INVALID_IDENTIFIERS), result.body)
    }

    @Test
    fun `should fail if not a valid accesskey`() = runTest {
        // Given
        repository.save(agenda)

        // When
        val result = controller.reenableAgenda(agenda.id.toString(), admin.key.value)

        // Then
        assertEquals(FORBIDDEN, result.statusCode)
        assertEquals(ServerError.of(INVALID_ACCESS_KEY), result.body)
    }

    private val agenda = AgendaMother.monday().copy(availableHours = emptyList())
    private val expectedAgenda = agenda.copy(availableHours = AvailableHour.monday())
    private val admin = AdminMother.random()
}
