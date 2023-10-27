package com.codely.admin.application.validate

import arrow.core.raise.Raise
import com.codely.admin.application.validate.ValidateAccessKeyError.AdminNotFound
import com.codely.admin.application.validate.ValidateAccessKeyError.InvalidAccessKey
import com.codely.admin.domain.AccessKey
import com.codely.admin.domain.AdminFindByCriteria.ByKey
import com.codely.admin.domain.AdminRepository
import com.codely.admin.domain.findByOrElse

context(AdminRepository, Raise<ValidateAccessKeyError>)
suspend fun validate(accessKey: AccessKey) {
    val admin = findByOrElse(ByKey(accessKey)) { AdminNotFound }

    if (admin.key != accessKey) raise(InvalidAccessKey)
    else Unit
}

sealed class ValidateAccessKeyError {
    data object InvalidAccessKey : ValidateAccessKeyError()
    data object AdminNotFound : ValidateAccessKeyError()
}
