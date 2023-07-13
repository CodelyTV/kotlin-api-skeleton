package com.codely.agenda.secondaryadapter.database.document

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaAgendaRepository : MongoRepository<AgendaDocument, String>
