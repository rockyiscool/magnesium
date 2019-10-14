package com.rc.magnesium.endpoint.v1

import com.rc.magnesium.endpoint.EndpointSpecification
import com.rc.magnesium.endpoint.v1.MemberEndpoint
import com.rc.magnesium.entity.Member
import com.rc.magnesium.exception.EmailExistsException
import com.rc.magnesium.payload.request.v1.MemberRequest
import com.rc.magnesium.service.CreateMember
import groovy.json.JsonBuilder
import spock.lang.Unroll

import static io.github.benas.randombeans.api.EnhancedRandom.random
import static org.hamcrest.Matchers.containsString
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class MemberEndpointTests extends EndpointSpecification {

    def createMemberMock

    def setup() {
        this.createMemberMock = Mock(CreateMember)
        init(new MemberEndpoint(createMemberMock))
    }

    def "Should return 201 with POST /members"() {

        given:
        MemberRequest request = random(MemberRequest.class)
        def requestJson = new JsonBuilder(request).toString()

        and:
        Member member = new Member()
        member.id = UUID.randomUUID()
        createMemberMock.create(*_) >> member

        when:
        def response = mockMvc.perform(post("/v1/members")
                .contentType(APPLICATION_JSON)
                .content(requestJson))

        then:
        response.andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("/v1/members/" + member.id)))
    }

    @Unroll
    def "Should return 400 with POST /members due to a(an) #testCase"() {

        given:
        def requestJson = new JsonBuilder(request).toString()

        when:
        def response = mockMvc.perform(post("/v1/members")
                .contentType(APPLICATION_JSON)
                .content(requestJson))

        then:
        response.andExpect(status().isBadRequest())

        def responseBody = response.andReturn().getResponse().getContentAsString()
        responseBody.matches("\\{\"traceId\":(.+),\"spanId\":(.+),\"code\":\"0x000001\",\"message\":(.+)\\}")
        responseBody.contains(message)


        where:
        testCase         | request                                                                                  | message
        "empty name"     | new MemberRequest(name: "", email: "rocky.chen@hotmail.co.uk", password: "123")          | "{\"name\":\"name\",\"message\":\"name can't empty!\"}"
        "null name"      | new MemberRequest(name: null, email: "rocky.chen@hotmail.co.uk", password: "123")        | "{\"name\":\"name\",\"message\":\"name can't empty!\"}"
        "empty email"    | new MemberRequest(name: "Rocky Chen", email: "", password: "123")                        | "{\"name\":\"email\",\"message\":\"email can't empty!\"}"
        "null email"     | new MemberRequest(name: "Rocky Chen", email: null, password: "123")                      | "{\"name\":\"email\",\"message\":\"email can't empty!\"}"
        "empty password" | new MemberRequest(name: "Rocky Chen", email: "rocky.chen@hotmail.co.uk", password: "")   | "{\"name\":\"password\",\"message\":\"password can't empty!\"}"
        "null password"  | new MemberRequest(name: "Rocky Chen", email: "rocky.chen@hotmail.co.uk", password: null) | "{\"name\":\"password\",\"message\":\"password can't empty!\"}"
    }

    @Unroll
    def "Should return 400 with POST /members due to #testCase"() {

        given:
        def requestJson = new JsonBuilder(request).toString()

        when:
        def response = mockMvc.perform(post("/v1/members")
                .contentType(APPLICATION_JSON)
                .content(requestJson))

        then:
        response.andExpect(status().isBadRequest())

        def responseBody = response.andReturn().getResponse().getContentAsString()
        responseBody.matches("\\{\"traceId\":(.+),\"spanId\":(.+),\"code\":\"0x000001\",\"message\":(.+)\\}")
        responseBody.contains("{\"name\":\"name\",\"message\":\"name can't empty!\"}")
        responseBody.contains("{\"name\":\"email\",\"message\":\"email can't empty!\"}")
        responseBody.contains("{\"name\":\"password\",\"message\":\"password can't empty!\"}")


        where:
        testCase           | request
        "all empty fields" | new MemberRequest(name: "", email: "", password: "")
        "all null fields"  | new MemberRequest(name: null, email: null, password: null)
    }

    def "Should return 409 with POST /members when email is existence"() {

        given:
        MemberRequest request = random(MemberRequest.class)
        def requestJson = new JsonBuilder(request).toString()

        and:
        createMemberMock.create(*_) >> { throw new EmailExistsException() }

        when:
        def response = mockMvc.perform(post("/v1/members")
                .contentType(APPLICATION_JSON)
                .content(requestJson))

        then:
        response.andExpect(status().isConflict())
                .andReturn()

        and:
        def responseBody = response.andReturn().getResponse().getContentAsString()
        responseBody.matches("\\{\"traceId\":(.+),\"spanId\":(.+),\"code\":\"0x001001\",\"message\":(.+)\\}")
    }

    def "Should return 500 with POST /members when an unexpected error occurred"() {

        given:
        MemberRequest request = random(MemberRequest.class)
        def requestJson = new JsonBuilder(request).toString()

        and:
        createMemberMock.create(*_) >> { throw new IllegalStateException("An unexpected error occurred") }

        when:
        def response = mockMvc.perform(post("/v1/members")
                .contentType(APPLICATION_JSON)
                .content(requestJson))

        then:
        response.andExpect(status().isInternalServerError())
                .andReturn()

        and:
        def responseBody = response.andReturn().getResponse().getContentAsString()
        println(responseBody);
        responseBody.matches("\\{\"traceId\":(.+),\"spanId\":(.+),\"code\":\"0x999999\",\"message\":(.+)\\}")
    }
}
