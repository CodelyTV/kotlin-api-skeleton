package com.codely.shared.response

import arrow.core.Either
import org.springframework.http.ResponseEntity


inline fun <E, R> Either<E, R>.toServerResponse(
    onValidResponse: (R) -> ResponseEntity<*>,
    onError: (E) -> ResponseEntity<*>
): ResponseEntity<*> = fold({ onError(it) }, { onValidResponse(it) })
