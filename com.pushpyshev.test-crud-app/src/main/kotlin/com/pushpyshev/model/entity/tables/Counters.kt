package com.pushpyshev.model.entity.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object Counters : IntIdTable("counters") {
    val name = varchar("name", 255)
    val value = integer("value")
}