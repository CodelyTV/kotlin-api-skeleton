package com.codely.admin.fakes

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.codely.admin.domain.AccessKey
import com.codely.admin.domain.AccessKeyGenerator

class FakeAccessKeyGenerator : AccessKeyGenerator {
    private var keys = mutableListOf<AccessKey>()

    fun generateKey(accessKey: AccessKey) = keys.add(accessKey)

    override suspend fun generateKey(): Either<Throwable, AccessKey> = catch { keys.first() }
}
