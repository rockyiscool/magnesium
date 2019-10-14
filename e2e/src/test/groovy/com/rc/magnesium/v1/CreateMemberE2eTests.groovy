package com.rc.magnesium.v1

import com.rc.magnesium.E2eTestSpecification
import com.rc.magnesium.resource.HttpClientResourceRule
import com.rc.magnesium.resource.KafkaResourceRule
import com.rc.magnesium.resource.PostgresqlResourceRule
import groovy.json.JsonSlurper
import org.apache.http.HttpHeaders
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.client.methods.RequestBuilder
import org.apache.http.entity.StringEntity
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.json.JSONObject
import org.junit.Rule

import java.time.Duration

class CreateMemberE2eTests extends E2eTestSpecification {

    @Rule
    HttpClientResourceRule httpClientResource = new HttpClientResourceRule()

    @Rule
    PostgresqlResourceRule dbResource = new PostgresqlResourceRule(
            "postgres",
            5432,
            "magnesium",
            "admin",
            "admin123")

    @Rule
    KafkaResourceRule kafkaResource = new KafkaResourceRule("kafka:9092", "member-activity")

    def "Should insert the request data into database"() {
        given:
        JSONObject requestJson = new JSONObject()
                .put("name", "rocky")
                .put("email", UUID.randomUUID().toString() + "@email.test")
                .put("password", "Password123!")

        HttpUriRequest request = RequestBuilder.post()
                .setUri("http://magnesium/v1/members")
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setEntity(new StringEntity(requestJson.toString()))
                .build()

        when:
        CloseableHttpResponse response = httpClientResource.client.execute(request)

        then:
        response.getStatusLine().getStatusCode() == 201

        and:
        List<Map<String, Object>> memberData = getDbQueryResult(
                dbResource.connection,
                "SELECT * FROM members where email = ?",
                requestJson.getString("email"))

        memberData.get(0).get("id") != null
        requestJson.get("name") == memberData.get(0).get("name")
        requestJson.get("email") == memberData.get(0).get("email")
        memberData.get(0).get("created_date") != null
        memberData.get(0).get("last_modified_date") != null

        and:
        ConsumerRecords<byte[], byte[]> consumerRecords = getConsumerRecords(kafkaResource.consumer, Duration.ofSeconds(1))
        consumerRecords.count() == 1

        def jsonSlurper = new JsonSlurper()
        def record = jsonSlurper.parse(consumerRecords.toList().get(0).value())
        record.id == memberData.get(0).get("id").toString()
        record.name == memberData.get(0).get("name")
        record.email == memberData.get(0).get("email")
        record.password == requestJson.get("password")
    }
}
