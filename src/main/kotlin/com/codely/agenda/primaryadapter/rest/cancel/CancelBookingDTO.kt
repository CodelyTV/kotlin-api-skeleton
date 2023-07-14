package com.codely.agenda.primaryadapter.rest.cancel

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class CancelBookingDTO(
    @JsonProperty("playerName")
    val playerName: String
) {
    companion object {
        @JsonCreator
        fun create(playerName: String) = CancelBookingDTO(playerName)
    }
}
