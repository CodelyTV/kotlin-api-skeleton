package com.codely.admin.domain

interface AccessKeyGenerator {
    suspend fun generateKey(): AccessKey
}
