package com.codely.admin.fakes

import com.codely.admin.domain.Password
import com.codely.admin.domain.PasswordEncrypter

class FakePasswordEncrypter : PasswordEncrypter {
    override suspend fun encrypt(password: Password): Password = password
}
