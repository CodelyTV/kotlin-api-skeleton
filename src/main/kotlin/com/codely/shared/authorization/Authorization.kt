package com.codely.shared.authorization

import arrow.core.raise.Raise
import arrow.core.raise.withError
import com.codely.admin.application.validate.validate
import com.codely.admin.domain.AccessKey
import com.codely.admin.domain.AdminRepository

context(AdminRepository, Raise<Error>)
suspend fun <Error, T> executeIfAllowed(accessKey: String, block: suspend () -> T, recover: suspend () -> Error): T {
    withError(block = { validate(AccessKey(accessKey)) }, transform = { recover() })
    return block()
}
