package com.codely.admin.fakes

import com.codely.admin.domain.AccessKey
import com.codely.admin.domain.Admin
import com.codely.admin.domain.AdminFindByCriteria
import com.codely.admin.domain.AdminFindByCriteria.ByKey
import com.codely.admin.domain.AdminRepository
import com.codely.admin.domain.Password
import com.codely.admin.domain.Username
import com.codely.shared.fakes.FakeRepository

class FakeAdminRepository : AdminRepository, FakeRepository<Admin> {
    override val elements = mutableListOf<Admin>()

    override suspend fun save(admin: Admin) { elements.add(admin) }

    override suspend fun find(criteria: AdminFindByCriteria): Admin? =
        when (criteria) {
            is ByKey -> elements.firstOrNull { it.key == criteria.accessKey }
        }

    override suspend fun signIn(username: Username, password: Password): AccessKey? =
        elements.firstOrNull { it.username == username && it.password == password }?.key
}
