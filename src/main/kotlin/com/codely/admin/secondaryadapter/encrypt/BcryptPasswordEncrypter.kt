package com.codely.admin.secondaryadapter.encrypt

import com.codely.admin.domain.Password
import com.codely.admin.domain.PasswordEncrypter
import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Component

@Component
class BcryptPasswordEncrypter : PasswordEncrypter {
    private val saltRounds = 12 // Number of salt rounds for hashing

    override suspend fun encrypt(password: Password): Password =
        BCrypt.gensalt(saltRounds)
            .let { salt -> BCrypt.hashpw(password.value, salt) }
            .let { encryptedPassword -> Password(encryptedPassword) }
}
