package com.codely.competition.players.infrastructure.database

import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.players.domain.*
import com.codely.competition.players.domain.FindPlayerCriteria.ByClubLeagueAndName
import com.codely.competition.players.domain.FindPlayerCriteria.ById
import com.codely.competition.players.domain.SearchPlayerCriteria.ByClub
import com.codely.competition.ranking.domain.League
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

interface JpaPlayerRepository : MongoRepository<PlayerDocument, Long> {
    fun findAllByClub(club: String): List<PlayerDocument>
    fun findByClubAndInitialLeagueAndNameContaining(club: String, league: String, name: String): PlayerDocument?
}

@Document(collection = "Players")
data class PlayerDocument(
    @Id
    val id: Long,
    val name: String,
    val club: String,
    val initialRanking: Int,
    val initialLeague: String,
    val promotedToHigherLeagues: Boolean
) {
    fun toPlayer(): Player =
        Player(
            id = id,
            name = name,
            clubName = ClubName(club),
            initialRanking = initialRanking,
            initialLeague = League.valueOf(initialLeague),
            promotedToHigherLeagues = promotedToHigherLeagues
        )
}

internal fun Player.toDocument(): PlayerDocument =
    PlayerDocument(
        id = id,
        name = name,
        club = clubName.value,
        initialRanking = initialRanking,
        initialLeague = initialLeague.name,
        promotedToHigherLeagues = promotedToHigherLeagues
    )

@Component
class MongoPlayerRepository(private val repository: JpaPlayerRepository): PlayerRepository {
    override suspend fun save(player: Player) { repository.save(player.toDocument()) }

    override suspend fun find(criteria: FindPlayerCriteria): Player? =
        when(criteria) {
            is ById -> repository.findByIdOrNull(criteria.id)
            is ByClubLeagueAndName -> repository.findByClubAndInitialLeagueAndNameContaining(criteria.club.value, criteria.league.name, criteria.name)
        }?.toPlayer()

    override suspend fun search(criteria: SearchPlayerCriteria): List<Player> =
        when(criteria) {
            is ByClub -> repository.findAllByClub(criteria.club.value)
        }.map { it.toPlayer() }

    override suspend fun exists(criteria: ExistsPlayerCriteria): Boolean =
        when(criteria) {
            is ExistsPlayerCriteria.ById -> repository.existsById(criteria.id)
        }
}
