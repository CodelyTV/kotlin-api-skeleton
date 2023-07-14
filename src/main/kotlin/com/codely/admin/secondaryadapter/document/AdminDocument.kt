package com.codely.admin.secondaryadapter.document

import com.codely.admin.domain.AccessKey
import com.codely.admin.domain.Admin
import com.codely.admin.domain.Password
import com.codely.admin.domain.Username
import java.util.UUID
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "Admins")
data class AdminDocument(
    val id: String,
    val username: String,
    val password: String,
    val accessKey: String
) {
    fun toAdmin() = Admin(
        id = UUID.fromString(id),
        username = Username(value = username),
        password = Password(value = password),
        key = AccessKey(value = accessKey)
    )
}

internal fun Admin.toDocument() =
    AdminDocument(
        id = id.toString(),
        username = username.value,
        password = password.value,
        accessKey = key.value
    )
