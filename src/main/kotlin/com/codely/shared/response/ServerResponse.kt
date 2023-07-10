package com.codely.shared.response

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

inline fun <E> Either<E, Any>.toServerResponse(toError: (E) -> ResponseEntity<*>): ResponseEntity<*> =
    fold({ toError(it) }, { ResponseEntity.status(HttpStatus.NO_CONTENT).body(null) })

inline fun <E, R> Either<E, R>.toServerResponse(
    onValidResponse: (R) -> ResponseEntity<*>,
    onError: (E) -> ResponseEntity<*>
): ResponseEntity<*> = fold({ onError(it) }, { onValidResponse(it) })
