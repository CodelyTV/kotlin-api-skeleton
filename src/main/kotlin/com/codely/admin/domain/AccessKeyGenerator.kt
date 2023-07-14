package com.codely.admin.domain

import arrow.core.Either

interface AccessKeyGenerator {
    suspend fun generateKey(): Either<Throwable, AccessKey>
}

context(AccessKeyGenerator)
suspend fun <T> generateKeyOrElse(onError: (cause: Throwable) -> T): Either<T, AccessKey> =
    generateKey()
        .mapLeft { error -> onError(error) }
