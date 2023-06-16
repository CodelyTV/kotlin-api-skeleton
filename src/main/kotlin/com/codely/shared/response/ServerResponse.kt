package com.codely.shared.response

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

inline fun <E> Either<E, Any>.toServerResponse(toError: (E) -> ResponseEntity<*>): ResponseEntity<*> =
    fold({ toError(it) }, { ResponseEntity.status(HttpStatus.OK).body(REQUEST_PROCESSED_MESSAGE) })

const val REQUEST_PROCESSED_MESSAGE = "Request processed successfully"
