package com.codely.agenda.disable

import com.codely.admin.AdminMother
import com.codely.admin.fakes.FakeAdminRepository
import com.codely.admin.primaryadapter.rest.error.AdminServerErrors.INVALID_ACCESS_KEY
import com.codely.agenda.AgendaMother
import com.codely.agenda.fakes.FakeAgendaRepository
import com.codely.agenda.primaryadapter.rest.disable.DisableAgendaController
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.AGENDA_DOES_NOT_EXIST
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.INVALID_IDENTIFIERS
import com.codely.shared.error.ServerError
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.FORBIDDEN

@ExperimentalCoroutinesApi
class DisableAgendaTest {

    private val repository = FakeAgendaRepository()
    private val adminRepository = FakeAdminRepository()

    private val controller = DisableAgendaController(repository, adminRepository)

    @BeforeEach
    fun setUp() {
        repository.resetFake()
        adminRepository.resetFake()
    }

    @Test
    fun `should remove all available hours for the given agenda`() = runTest {
        // Given
        repository.save(agenda)
        adminRepository.save(admin)

        // When
        val result = controller.disableAgenda(agenda.id.toString(), admin.key.value)

        // Then
        assertEquals(OK, result.statusCode)
        assertEquals(expectedAgenda, result.body)
    }

    @Test
    fun `should not fail if agenda does not have available hours`() = runTest {
        // Given
        repository.save(agenda.copy(availableHours = emptyList()))
        adminRepository.save(admin)

        // When
        val result = controller.disableAgenda(agenda.id.toString(), admin.key.value)

        // Then
        assertEquals(OK, result.statusCode)
        assertEquals(expectedAgenda, result.body)
    }

    @Test
    fun `should fail if agenda does not exist`() = runTest {
        // Given
        adminRepository.save(admin)

        // When
        val result = controller.disableAgenda(agenda.id.toString(), admin.key.value)

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
        val result = controller.disableAgenda("1234532", admin.key.value)

        // Then
        assertEquals(BAD_REQUEST, result.statusCode)
        assertEquals(ServerError.of(INVALID_IDENTIFIERS), result.body)
    }

    @Test
    fun `should fail if not a valid accesskey`() = runTest {
        // Given
        repository.save(agenda)

        // When
        val result = controller.disableAgenda(agenda.id.toString(), admin.key.value)

        // Then
        assertEquals(FORBIDDEN, result.statusCode)
        assertEquals(ServerError.of(INVALID_ACCESS_KEY), result.body)
    }

    private val agenda = AgendaMother.fullyBooked()
    private val expectedAgenda = agenda.copy(availableHours = emptyList())
    private val admin = AdminMother.random()
}
