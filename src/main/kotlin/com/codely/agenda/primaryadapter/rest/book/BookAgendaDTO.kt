package com.codely.agenda.primaryadapter.rest.book

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class BookAgendaDTO(
    @JsonProperty("playerName")
    val playerName: String
) {
    companion object {
        @JsonCreator
        fun create(playerName: String) = BookAgendaDTO(playerName)
    }
}
