package com.codely.competition.ranking

import com.codely.competition.ranking.domain.GameStats
import com.codely.competition.ranking.domain.RankedPlayer
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File

object WeeklyRankingUpdater {

    fun processRankingPDF(pdfFile: File) {
        val pdDocument = PDDocument.load(pdfFile)
        val textStripper = PDFTextStripper()
        val text = textStripper.getText(pdDocument)

        val playersFromOurGroup = text.split("\n")
            .filter { line -> LEAGUE_TEAM_NAMES.any { it in line } }
            .map { mapToPlayer(it) }

        playersFromOurGroup
            .map { println(it) }

        pdDocument.close()
    }

    private fun mapToPlayer(input: String): RankedPlayer {
        val updatedInput = formatInput(input)
        val elements = updatedInput.split(" ")
        val club = findClub(input)
        val playerName = findPlayerName(input)
        val gameStats = findGameStats(input)

        val (id, ranking) = findRankingAndId(elements[0], gameStats.winRate)

        return RankedPlayer(id, playerName, club, gameStats, ranking)
    }

    private fun formatInput(input: String): String {
        val clubName = findClub(input)

        return input.replace(clubName, "$clubName ")
    }
    
    private fun findPlayerName(input: String): String {
        val clubName = findClub(input)

        return input
            .substringAfter(clubName)
            .trim()
            .split(" ")
            .take(2)
            .reversed()
            .joinToString { it }
            .uppercase()
            .replace(",", "")
    }

    private fun findGameStats(input: String): GameStats {
        val statInput = input.split(" ")
            .takeLast(4)
            .joinToString { it }

        val x = statInput
            .split(",")[0]
            .split("%")

        val winRate = x[0].toDouble()
        val games = x[1]

        val gameStats =
            when {
                games.toLong() > 1000 -> games.createGameStatFor(2)
                else -> games.createGameStatFor(1)
            }

        return GameStats(gameStats.first, gameStats.second, gameStats.gamesLost(), winRate)
    }

    private fun String.createGameStatFor(number: Int): Pair<Int, Int> = Pair(this.takeLast(number).toInt(), this.take(number).toInt())
    private fun Pair<Int, Int>.gamesLost(): Int = this.first - this.second
    private fun findClub(input: String) = LEAGUE_TEAM_NAMES.find { it in input } ?: "CLUB NOT FOUND"
    private fun findRankingAndId(input: String, winRate: Double): Pair<Long, Int> {
        val id = PLAYER_IDS_OVER_THOUSAND.find { it in input }

        if (winRate == 0.0) return Pair(input.takeLast(input.length - 2).toLong(), input.take(2).toInt())

        return id
            ?.let { Pair(it.toLong(), input.take(4).toInt()) }
            ?: Pair(input.takeLast(input.length - 3).toLong(), input.take(3).toInt())
    }

    private val PLAYER_IDS_OVER_THOUSAND = listOf("456", "582")
    private val LEAGUE_TEAM_NAMES = listOf(
        "CPP IGUALADA",
        "TERMOTUR CALELLA",
        "TT SANT ANDREU -A-",
        "CLUB ARIEL",
        "ÀNECBLAU - TT ELS JOVES",
        "CTT BARCELONA ''B''",
        "UE SANT CUGAT",
        "FALCONS SABADELL A.E.",
        "CTT SALLENT",
        "TOT MOTOR CTT RIPOLLET ''B''",
        "TT PARETS",
        "CTT VALLS DE TORROELLA"
    )
}
