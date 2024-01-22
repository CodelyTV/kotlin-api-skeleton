package com.codely.competition.ranking.application.update

import com.codely.competition.clubs.domain.Club
import com.codely.competition.clubs.domain.ClubRepository
import com.codely.competition.players.domain.FindPlayerCriteria.ByClubAndName
import com.codely.competition.players.domain.PlayerRepository
import com.codely.competition.ranking.domain.GameStats
import com.codely.competition.ranking.domain.RankedPlayer
import com.codely.competition.ranking.domain.RankedPlayerRepository

class RankingUpdater(
    private val playerRepository: PlayerRepository,
    private val clubRepository: ClubRepository,
    private val rankedPlayerRepository: RankedPlayerRepository
) {
    
    suspend operator fun invoke(lines: List<String>) {
        val clubs = clubRepository.search()
            .map { it.name }


        val playersFromOurGroup = lines
            .filter { line -> clubs.any { it in line } }
            .map { mapToPlayer(it, clubs) }

        playersFromOurGroup.map { rankedPlayerRepository.save(it) }
    }

    private suspend fun mapToPlayer(input: String, clubs: List<String>): RankedPlayer {
        val club = clubs.first { it in input }
        val playerName = findPlayerName(input, clubs)

        val player = playerRepository.find(ByClubAndName(Club(club), playerName))

        val updatedInput = input.formatInput(clubs)
        val elements = updatedInput.split(" ")
        
        return player?.let {
            val gameStats = findGameStats(input)
            val ranking = elements[0].replace(player.id.toString(), "").toInt()
            RankedPlayer(it.id, it.name, club, gameStats, ranking)
        } ?: createRankedPlayerFromData(input, club, playerName, clubs)
    }
    
    private fun createRankedPlayerFromData(input: String, club: String, playerName: String, clubs: List<String>): RankedPlayer {
        val updatedInput = input.formatInput(clubs)
        val elements = updatedInput.split(" ")
        val gameStats = findGameStats(input)

        val (id, ranking) = findRankingAndId(elements[0], gameStats.winRate)

        return RankedPlayer(id, playerName, club, gameStats, ranking)
    }

    private fun findPlayerName(input: String, clubs: List<String>): String {
        val clubName = clubs.first { it in input }

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
                games.length >= 4 -> Pair(games.takeLast(2).toInt(), games.take(2).toInt())
                games.length == 3 -> Pair(games.takeLast(2).toInt(), games.take(1).toInt())
                else -> Pair(games.takeLast(1).toInt(), games.take(1).toInt())
            }

        return GameStats(gameStats.first, gameStats.second, gameStats.gamesLost(), winRate)
    }

    private fun Pair<Int, Int>.gamesLost(): Int = this.first - this.second
    private fun findRankingAndId(input: String, winRate: Double): Pair<Long, Int> {
        val id = PLAYER_IDS_OVER_THOUSAND.find { it in input }

        if (winRate == 0.0) return Pair(input.takeLast(input.length - 2).toLong(), input.take(2).toInt())

        return id
            ?.let { Pair(it.toLong(), input.take(4).toInt()) }
            ?: Pair(input.takeLast(input.length - 3).toLong(), input.take(3).toInt())
    }

    private fun String.formatInput(clubs: List<String>): String {
        val clubName = clubs.first { it in this }
        return this.replace(clubName, "$clubName ")
    }

    private val PLAYER_IDS_OVER_THOUSAND = listOf("456", "582")
}
