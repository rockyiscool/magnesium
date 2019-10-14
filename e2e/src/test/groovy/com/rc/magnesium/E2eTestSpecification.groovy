package com.rc.magnesium


import org.apache.commons.dbutils.QueryRunner
import org.apache.commons.dbutils.handlers.MapListHandler
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecords
import spock.lang.Specification

import java.sql.Connection
import java.sql.SQLException
import java.time.Duration

class E2eTestSpecification extends Specification {

    List<Map<String, Object>> getDbQueryResult(final Connection connection, final String sql, Object... params) throws SQLException {
        MapListHandler mapListHandler = new MapListHandler()
        QueryRunner queryRunner = new QueryRunner()
        List<Map<String, Object>> result = queryRunner.query(
                connection,
                sql,
                params,
                mapListHandler
        )

        return result
    }

    int executeDbQuery(final Connection connection, final String sql, Object... params) throws SQLException {
        QueryRunner queryRunner = new QueryRunner()
        int changes = queryRunner.update(connection, sql, params)

        return changes
    }

    ConsumerRecords<byte[], byte[]> getConsumerRecords(Consumer<byte[], byte[]> consumer, Duration timeout) {
        final int maxRetry = 10;
        int retryCount = 0;

        while (true) {
            def records = consumer.poll(timeout)
            if (records.count() == 0) {
                retryCount++;
                if (retryCount > maxRetry) {
                    println("@@@@@@@@ break retryCount: " + retryCount)
                    break;
                } else {
                    println("@@@@@@@@ continue retryCount: " + retryCount)
                    continue;
                }
            }

            consumer.commitAsync()

            println("@@@@@@@@ retryCount: " + retryCount)
            return records
        }
    }
}
