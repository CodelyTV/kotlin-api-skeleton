package com.codely.competition.results

import com.codely.competition.calendar.Calendar
import com.codely.competition.calendar.Match
import com.codely.competition.clubs.domain.Club
import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.clubs.domain.ClubRepository
import com.codely.competition.clubs.domain.SearchClubCriteria
import com.codely.competition.clubs.domain.SearchClubCriteria.All
import kotlinx.coroutines.runBlocking
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.springframework.core.io.ClassPathResource
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.time.ZonedDateTime
import java.util.*

//@Component
class WeeklyResultUpdater(
    private val clubRepository: ClubRepository
) {

//    @EventListener(ApplicationReadyEvent::class)
    fun processCalendarPDF() = runBlocking {
        val calendarPdfResource = ClassPathResource("/pdf/calendari-3A.pdf")
        val tempCalendarPdfFile = Files.createTempFile("temp-pdf-calendari-3A", ".pdf").toFile()
        Files.copy(calendarPdfResource.inputStream, tempCalendarPdfFile.toPath(), StandardCopyOption.REPLACE_EXISTING)

        val pdDocument = PDDocument.load(tempCalendarPdfFile)
        val textStripper = PDFTextStripper()
        val text = textStripper.getText(pdDocument)

        val splitList = text.split("\n")

        val leagueStandings =
            splitList.subList(6, 18)
                .map { it.replace(Regex("[0-9]"), "").trim() }

        val weekEndResults = splitList
            .subList(leagueStandings.size + 6, splitList.size)
            .getResultsFrom(leagueStandings)

        weekEndResults.forEach { key, value ->
            Calendar(
                id = UUID.randomUUID(),
                name = key,
                matches = value
                    .map {
                        Match(
                            localClub = ClubName(""),
                            visitorClub = ClubName(""),
                            result = null,
                            dateTime = ZonedDateTime.now()
                        )
                    }

            )
        }
        pdDocument.close()
    }

    private suspend fun List<String>.getResultsFrom(teams: List<String>): Map<String, List<Match>> {
        val result = mutableMapOf<String, List<Match>>()

        var currentKey: String? = null
        var currentList: MutableList<Match>? = null

        for (line in this) {
            val matchResult = Regex("(\\d*)Jornada Acta").find(line)
            val numberBeforeKeyword = matchResult?.groupValues?.get(1)

            if (numberBeforeKeyword != null) {
                // If a new keyword is found, save the current list to the map
                currentKey?.let { key ->
                    currentList?.let { list ->
                        result[key] = list.toList()
                    }
                }

                // Start a new list for the new keyword
                currentKey = "$numberBeforeKeyword Jornada Acta"
                currentList = mutableListOf()
            } else if (teams.any { it in line }) {
                // Add lines to the current list
                val finalLine = createFinalLine(line)
                val match = finalLine.toMatch()
                currentList?.add(match)
            }
        }

        // Save the last list to the map
        currentKey?.let { key ->
            currentList?.let { list ->
                result[key] = list.toList()
            }
        }

        return result
    }

    private fun createFinalLine(line: String): String {
        val x = line.removeRange(0, 8)
        return x.removeRange(x.length - 9, x.length).trim()
    }

    private suspend fun String.toMatch(): Match {
        val clubs = clubRepository.search(All).map { it.clubName.value }

        val club1 = clubs.last { it in this }
        val club2 = clubs.first { it in this }

        val localClub = clubs.minByOrNull { this.indexOf(it) } ?: "N/A"
        return Match(
            localClub = ClubName(localClub),
            visitorClub = ClubName(""),
            result = null,
            dateTime = ZonedDateTime.now()
        )
    }
}
