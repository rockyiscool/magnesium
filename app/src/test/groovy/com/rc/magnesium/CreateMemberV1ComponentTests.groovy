package com.rc.magnesium

import com.rc.magnesium.payload.event.v1.MemberEvent
import com.rc.magnesium.payload.request.v1.MemberRequest
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.springframework.messaging.Message

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CreateMemberV1ComponentTests extends ComponentTestSpecification {

    def "Should return 201 and save a member and an event"() {

        given:
        def request = new MemberRequest()
        request.name = "rocky"
        request.email = generateEmail()
        request.password = "password123!"
        def requestJson = new JsonBuilder(request).toString()

        when:
        def response = mockMvc.perform(post("/v1/members")
                .contentType(APPLICATION_JSON)
                .content(requestJson))

        then:
        response.andExpect(status().isCreated())
        and:
        def memberRow = sql.firstRow("select * from members where email = ?", request.email)
        memberRow.id != null
        memberRow.name == request.name
        memberRow.email == request.email
        memberRow.created_by != null
        memberRow.created_date != null
        memberRow.last_modified_by != null
        memberRow.last_modified_date != null
        memberRow.version == 0

        and: "should publish the events"
        Message<MemberEvent> received = (Message<MemberEvent>) collector.forChannel(memberActivityStreamMock.outbound()).poll()
        received.headers.get("X-EVENT") == "memberCreated"

        def jsonSlurper = new JsonSlurper()
        def payload = jsonSlurper.parseText(received.payload)
        payload.id == memberRow.id.toString()
        payload.name == memberRow.name
        payload.email == memberRow.email
        payload.password == request.password
    }

//    def assertDateTime(String date1, java.sql.Timestamp date2) {
//        def pattern = "yyyy-MM-dd HH:mm:ss"
//
//        def d1 = DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(date1)
//        def dstr1 = DateTimeFormatter.ofPattern(pattern).format(d1)
//
//        def dstr2 = new SimpleDateFormat(pattern).format(date2)
//
//        dstr1 == dstr2
//    }
}
