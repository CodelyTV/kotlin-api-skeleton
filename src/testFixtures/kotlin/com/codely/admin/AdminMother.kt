package com.codely.admin

import com.codely.admin.domain.AccessKey
import com.codely.admin.domain.Admin
import com.codely.admin.domain.Password
import com.codely.admin.domain.Username
import java.util.UUID

object AdminMother {

    fun random(
        id: UUID = UUID.randomUUID(),
        username: Username = Username("sant-andreu"),
        password: Password = Password("123password"),
        key: AccessKey = AccessKey("!!@#$!@AASDCFases11234")
    ) = Admin(
        id = id,
        username = username,
        password = password,
        key = key
    )
}
