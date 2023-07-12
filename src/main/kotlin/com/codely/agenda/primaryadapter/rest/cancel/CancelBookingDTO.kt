package com.codely.agenda.primaryadapter.rest.cancel

import java.util.UUID

data class CancelBookingDTO(val playerName: String, val availableHourId: UUID)
