package com.codely.admin.fakes

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.codely.admin.domain.Admin
import com.codely.admin.domain.AdminFindByCriteria
import com.codely.admin.domain.AdminFindByCriteria.Key
import com.codely.admin.domain.AdminFindByCriteria.Username
import com.codely.admin.domain.AdminRepository
import com.codely.shared.fakes.FakeRepository

class FakeAdminRepository : AdminRepository, FakeRepository<Admin> {
    override val elements = mutableListOf<Admin>()

    override suspend fun save(admin: Admin): Either<Throwable, Unit> = catch { elements.add(admin) }

    override suspend fun findBy(criteria: AdminFindByCriteria): Either<Throwable, Admin> = catch {
        when (criteria) {
            is Key -> elements.first { it.key == criteria.accessKey }
            is Username -> elements.first { it.username == criteria.username }
        }
    }
}
