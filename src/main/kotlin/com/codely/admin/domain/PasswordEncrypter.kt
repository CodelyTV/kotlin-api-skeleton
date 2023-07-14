package com.codely.admin.domain

import arrow.core.Either

interface PasswordEncrypter {
    suspend fun encrypt(password: Password): Either<Throwable, Password>
}

context(PasswordEncrypter)
suspend fun <T> Password.encryptOrElse(onError: (cause: Throwable) -> T): Either<T, Password> =
    encrypt(this)
        .mapLeft { error -> onError(error) }
