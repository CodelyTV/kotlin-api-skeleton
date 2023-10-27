package com.codely.admin.domain

interface PasswordEncrypter {
    suspend fun encrypt(password: Password): Password
}
