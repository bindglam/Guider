package com.bindglam.guider.util

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.bukkit.plugin.Plugin
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DatabaseUtils {
    private const val SQLITE_VALID_TIMEOUT = 500
    private var SQLITE_DATABASE_URL = "jdbc:sqlite:plugins/Guider/database.db"

    private var sqliteConnection: Connection? = null
    private var hikariDS: HikariDataSource? = null

    val connection: Connection
        get() {
            if(hikariDS != null) {
                return hikariDS!!.connection
            } else {
                ensureSQLiteConnection()

                return sqliteConnection!!
            }
        }

    fun init(plugin: Plugin) {
        when(plugin.config.getString("database.type")!!.uppercase()) {
            "SQLITE" -> {
                Class.forName("org.sqlite.JDBC")
            }

            "MYSQL" -> {
                val hikariConfig = HikariConfig()
                hikariConfig.driverClassName = "com.mysql.cj.jdbc.Driver"
                hikariConfig.jdbcUrl = "jdbc:mysql://" + plugin.config.getString("database.mysql.host") + ":" + plugin.config.getString("database.mysql.port") + "/" + plugin.config.getString("database.mysql.database") + "?useUnicode=true&characterEncoding=utf8"
                hikariConfig.username = plugin.config.getString("database.mysql.user")
                hikariConfig.password = plugin.config.getString("database.mysql.password")
                hikariConfig.maximumPoolSize = 10

                hikariDS = HikariDataSource(hikariConfig)
            }
        }
    }

    fun close() {
        if(hikariDS != null && !hikariDS!!.isClosed) {
            hikariDS!!.close()
        } else {
            closeSQLiteConnection()
        }
    }

    private fun createSQLiteConnection() {
        sqliteConnection = DriverManager.getConnection(SQLITE_DATABASE_URL)
    }

    private fun closeSQLiteConnection() {
        try {
            if(sqliteConnection?.isClosed == false)
                sqliteConnection?.close()
        } catch (e: SQLException) {
            throw RuntimeException(e)
        } finally {
            sqliteConnection = null
        }
    }

    private fun ensureSQLiteConnection() {
        try {
            if(sqliteConnection == null || sqliteConnection?.isValid(SQLITE_VALID_TIMEOUT) == false || sqliteConnection?.isClosed == true) {
                closeSQLiteConnection()
                createSQLiteConnection()
            }
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }
}