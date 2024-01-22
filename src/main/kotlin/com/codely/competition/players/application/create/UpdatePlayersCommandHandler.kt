package com.codely.competition.players.application.create

import com.codely.competition.clubs.application.create.ClubsCreator
import com.codely.competition.clubs.domain.ClubRepository
import com.codely.competition.players.domain.Player
import com.codely.competition.players.domain.PlayerRepository
import kotlinx.coroutines.runBlocking
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.springframework.stereotype.Component
import java.io.File

data class UpdatePlayerCommand(
    val playerListText: List<String>
)

@Component
class UpdatePlayersCommandHandler(
    private val repository: PlayerRepository,
    clubRepository: ClubRepository
) {

    private val createClubs = ClubsCreator(clubRepository)

    fun handle(command: UpdatePlayerCommand) = runBlocking {
        val sanitizedList = command.playerListText.subList(4, command.playerListText.size)

        val clubs = sanitizedList
            .filter { it.isNotEmpty() && !it.first().isDigit() }
            .filter { line -> !BLACKLISTED_KEYWORDS.any { it in line } }

        val groupedPlayers = groupByClub(
            sanitizedList.filter { line -> !BLACKLISTED_KEYWORDS.any { it in line } }, clubs
        )

        groupedPlayers.values
            .map { clubPlayers -> clubPlayers.map { repository.save(it) } }

        createClubs(clubs)
    }

    private fun groupByClub(inputList: List<String>, clubNames: List<String>): Map<String, List<Player>> {
        val result = mutableMapOf<String, MutableList<Player>>()
        var currentClubName = ""

        for (input in inputList) {
            if (clubNames.any { input.startsWith(it) }) {
                currentClubName = input
                result[currentClubName] = mutableListOf()
            } else if (currentClubName.isNotEmpty() && input.isNotBlank()) {
                val player = mapToPlayer(input, currentClubName)
                result[currentClubName]?.add(player)
            }
        }

        return result
    }

    private fun mapToPlayer(input: String, clubName: String): Player {
        return input.split(" ")
            .let { elements ->
                Player.create(
                    id = elements[0].toLong(),
                    name = "${elements[2]} ${elements[1]}".uppercase(),
                    club = clubName,
                    initialRanking = elements.getInitialRanking().toInt(),
                    promotedToHigherLeagues = LEAGUE_KEYWORDS.any { it in elements.last() }
                )
            }
    }

    private fun List<String>.getInitialRanking(): String {
        val last = last()
        return when {
            LEAGUE_KEYWORDS.any { it in last } -> get(size - 2).removeInscriptionDate()
            last.contains("/") -> last.removeInscriptionDate()
            else -> last()
        }
    }

    private fun String.removeInscriptionDate(): String =
        when {
            contains("/") -> removeRange(this.length - 8, this.length)
            else -> this
        }

    private val BLACKLISTED_KEYWORDS = listOf(
        "Representació Territorial",
        "Federació Catalana",
        "C/Duquessa d'Orleans",
        "RK Inicial",

    )
    private val LEAGUE_KEYWORDS = listOf("PREF", "1a", "2aA", "2aB", "3aA", "3aB")
}
