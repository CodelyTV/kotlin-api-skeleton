package com.codely.agenda.search

import com.codely.agenda.AgendaMother
import com.codely.agenda.domain.Agenda
import com.codely.agenda.fakes.FakeAgendaRepository
import com.codely.agenda.primaryadapter.rest.search.SearchAgendaController
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

@ExperimentalCoroutinesApi
class SearchAgendasTest {

    private val repository = FakeAgendaRepository()
    private val controller = SearchAgendaController(repository)

    @BeforeEach
    fun setUp() {
        repository.resetFake()
    }

    @Test
    fun `should return current given week's agendas`() = runTest {
        // Given
        `agendas exists for given week`()

        // When
        val result = controller.search(agenda.week, agenda.year)

        // Then
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(expectedResponse, result.body)
    }

    @Test
    fun `should not fail if there are not week agendas`() = runTest {
        // Given

        // When
        val result = controller.search(agenda.week, agenda.year)

        // Then
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(emptyList<Agenda>(), result.body)
    }

    private suspend fun `agendas exists for given week`() {
        repository.save(agenda)
        repository.save(agenda2)
        repository.save(agenda3)
        repository.save(agenda4)
        repository.save(agenda5)
        repository.save(agenda6)
        repository.save(agenda7)
    }

    private val agenda = AgendaMother.random()
    private val agenda2 = AgendaMother.random(week = agenda.week, year = agenda.year)
    private val agenda3 = AgendaMother.random(week = agenda.week, year = agenda.year)
    private val agenda4 = AgendaMother.random(week = agenda.week, year = agenda.year)
    private val agenda5 = AgendaMother.random(week = agenda.week, year = agenda.year)
    private val agenda6 = AgendaMother.random(week = agenda.week, year = agenda.year)
    private val agenda7 = AgendaMother.random(week = agenda.week, year = agenda.year)

    private val expectedResponse = listOf(agenda, agenda2, agenda3, agenda4, agenda5, agenda6, agenda7)
}
