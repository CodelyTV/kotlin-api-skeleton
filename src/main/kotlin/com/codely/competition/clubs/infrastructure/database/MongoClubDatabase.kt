package com.codely.competition.clubs.infrastructure.database

import com.codely.competition.clubs.domain.*
import com.codely.competition.clubs.domain.ClubExistsCriteria.ByNameAndLeague
import com.codely.competition.clubs.domain.SearchClubCriteria.All
import com.codely.competition.clubs.domain.SearchClubCriteria.ByLeague
import com.codely.competition.ranking.domain.League
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import java.util.*

interface JpaClubRepository : MongoRepository<ClubDocument, String> {
    fun existsByNameAndLeague(name: String, league: String): Boolean
    fun findAllByLeague(name: String): List<ClubDocument>
}

@Document(collection = "Clubs")
data class ClubDocument(
    @Id
    val id: String,
    val name: String,
    val league: String
) {
    fun toClub(): Club = Club(ClubName(name), League.valueOf(league), id = UUID.fromString(id))
}

internal fun Club.toDocument(): ClubDocument = ClubDocument(id.toString(), clubName.value, league.name)

@Component
class MongoClubDatabase(private val repository: JpaClubRepository): ClubRepository {

    override suspend fun save(club: Club) { repository.save(club.toDocument()) }
    override suspend fun search(criteria: SearchClubCriteria): List<Club> =
        when(criteria) {
            All -> repository.findAll()
            is ByLeague -> repository.findAllByLeague(criteria.league.name)
        }.map { it.toClub() }

    override suspend fun exists(criteria: ClubExistsCriteria): Boolean =
        when(criteria) {
            is ByNameAndLeague -> repository.existsByNameAndLeague(criteria.clubName.value, criteria.league.name)
        }
}
