package com.codely.shared.error

data class ServerError(val code: String, val description: String? = null) {
    companion object {
        fun of(error: Pair<String, String>) = ServerError(error.first, error.second)
    }
}
