package com.codely.admin.secondaryadapter.accesskey

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.codely.admin.domain.AccessKey
import com.codely.admin.domain.AccessKeyGenerator
import kotlin.random.Random
import org.springframework.stereotype.Component

@Component
class AlphanumericAccessKeyGenerator : AccessKeyGenerator {
    override suspend fun generateKey(): Either<Throwable, AccessKey> = catch {
        val charPool = ('A'..'Z') + ('a'..'z') + ('0'..'9')

        val key = (1..25)
                .map { Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")

        AccessKey(key)
    }
}
