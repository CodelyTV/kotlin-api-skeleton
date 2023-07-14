package com.codely.admin.domain

import java.util.UUID

data class Admin(
    val id: UUID,
    val username: Username,
    val password: Password,
    val key: AccessKey
)

@JvmInline
value class Username(val value: String)
@JvmInline
value class Password(val value: String)
@JvmInline
value class AccessKey(val value: String)
