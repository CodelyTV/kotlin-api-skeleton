package com.codely.shared.response

import arrow.core.Either
import com.codely.shared.error.ServerError
import org.springframework.http.ResponseEntity

inline fun <E, R> Either<E, R>.toServerResponse(
    onValidResponse: (R) -> ResponseEntity<*>,
    onError: (E) -> ResponseEntity<*>
): ResponseEntity<*> = fold({ onError(it) }, { onValidResponse(it) })

fun ResponseEntity.BodyBuilder.withBody(error: Pair<String, String>): Response<ServerError> = body(ServerError.of(error))

fun ResponseEntity.BodyBuilder.withoutBody(): Response<ServerError> = body(null)

typealias Response<T> = ResponseEntity<T>
