package com.pushpyshev.plugin

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    private lateinit var hikariDataSource: HikariDataSource

    @Synchronized
    fun init(environment: ApplicationEnvironment) {
        val dbConfig = environment.config.config("ktor.database")
        val urlProperty = dbConfig.property("url").getString()
        val driverProperty = dbConfig.property("driver").getString()
        val userProperty = dbConfig.property("user").getString()
        val passwordProperty = dbConfig.property("password").getString()
        val maximumPoolSizeProperty = dbConfig.property("maximumPoolSize").getString().toInt()

        // HikariCP configuration
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = urlProperty
            driverClassName = driverProperty
            username = userProperty
            password = passwordProperty
            maximumPoolSize = maximumPoolSizeProperty
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_READ_COMMITTED"
            validate()
        }

        hikariDataSource = HikariDataSource(hikariConfig)

        // Connect Exposed to the database using HikariCP
        Database.connect(hikariDataSource)
        // Migrate with liquibase
        val liquibase = Liquibase("liquibase/db.changelog-master.xml", ClassLoaderResourceAccessor(), JdbcConnection(hikariDataSource.getConnection()))
        liquibase.update("main")
    }

    @Synchronized
    fun close() {
        hikariDataSource.close()
    }
}