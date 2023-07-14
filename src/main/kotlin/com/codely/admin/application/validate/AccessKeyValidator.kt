package com.codely.admin.application.validate

import arrow.core.raise.Raise
import com.codely.admin.application.validate.ValidateAccessKeyError.Unknown
import com.codely.admin.application.validate.ValidateAccessKeyError.InvalidAccessKey
import com.codely.admin.domain.AccessKey
import com.codely.admin.domain.AdminFindByCriteria.Key
import com.codely.admin.domain.AdminRepository
import com.codely.admin.domain.findByOrElse

context(AdminRepository, Raise<ValidateAccessKeyError>)
suspend fun validate(accessKey: AccessKey) {
    val admin = findByOrElse(Key(accessKey)) { error -> Unknown(error) }.bind()

    if (admin.key != accessKey) raise(InvalidAccessKey)
    else Unit
}

sealed class ValidateAccessKeyError {
    data object InvalidAccessKey : ValidateAccessKeyError()
    class Unknown(val cause: Throwable) : ValidateAccessKeyError()
}
