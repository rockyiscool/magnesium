package com.rc.magnesium.resource

import org.apache.commons.dbutils.DbUtils
import org.junit.rules.ExternalResource

import java.sql.Connection
import java.sql.DriverManager

class PostgresqlResourceRule extends ExternalResource {

    static final String CLASS_NAME = "org.postgresql.Driver"
    static final String CONNECTION_STRING_PATTERN = "jdbc:postgresql://%s:%s/%s"

    final String host
    final int port
    final String database
    final String user
    final String password

    Connection connection

    PostgresqlResourceRule(String host, int port, String database, String user, String password) {
        this.host = host
        this.port = port
        this.database = database
        this.user = user
        this.password = password
    }

    @Override
    protected void before() throws Throwable {
        Class.forName(CLASS_NAME)
        def connectionString = String.format(
                CONNECTION_STRING_PATTERN,
                host,
                port,
                database)
        this.connection = DriverManager.getConnection(connectionString, user, password)
    }

    @Override
    void after() {
        DbUtils.closeQuietly(connection)
    }
}