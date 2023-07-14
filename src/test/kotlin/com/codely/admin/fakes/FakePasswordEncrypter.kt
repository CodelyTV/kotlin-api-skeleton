package com.codely.admin.fakes

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.codely.admin.domain.Password
import com.codely.admin.domain.PasswordEncrypter

class FakePasswordEncrypter : PasswordEncrypter {
    override suspend fun encrypt(password: Password): Either<Throwable, Password> = catch { password }
}
