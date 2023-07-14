package com.codely.admin.secondaryadapter.encrypt

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.codely.admin.domain.Password
import com.codely.admin.domain.PasswordEncrypter
import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Component

@Component
class BcryptPasswordEncrypter : PasswordEncrypter {
    private val saltRounds = 12 // Number of salt rounds for hashing

    override suspend fun encrypt(password: Password): Either<Throwable, Password> = catch {
        BCrypt.gensalt(saltRounds)
            .let { salt -> BCrypt.hashpw(password.value, salt) }
            .let { encryptedPassword -> Password(encryptedPassword) }
    }
}
