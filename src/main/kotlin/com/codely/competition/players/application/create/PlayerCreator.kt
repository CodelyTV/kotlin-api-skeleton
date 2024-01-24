package com.codely.competition.players.application.create

import com.codely.competition.players.domain.ExistsPlayerCriteria.ById
import com.codely.competition.players.domain.Player
import com.codely.competition.players.domain.PlayerRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class PlayerCreator(private val repository: PlayerRepository) {

    operator suspend fun invoke(players: List<Player>): Unit = coroutineScope {
        players
            .forEach { player -> launch {
                if(!repository.exists(ById(player.id))) repository.save(player).also { println("Persisting player ${player.id} ${player.name} ${player.club}") }
                else Unit.also { println("Player ${player.id} ${player.name} ${player.club} already exists") }
            } }
    }
}
