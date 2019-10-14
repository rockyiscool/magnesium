package com.rc.magnesium.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.rc.magnesium.entity.Member
import com.rc.magnesium.exception.EmailExistsException
import com.rc.magnesium.payload.request.v1.MemberRequest
import com.rc.magnesium.repository.MemberRepository
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHeaders
import org.springframework.util.MimeTypeUtils
import spock.lang.Specification

import static io.github.benas.randombeans.api.EnhancedRandom.random

class CreateMemberTests extends Specification {

    CreateMember service
    MemberRepository memberRepositoryMock
    MemberActivityStream memberActivityStreamMock
    MessageChannel messageChannelMock
    ObjectMapper mapperMock

    def setup() {
        this.memberRepositoryMock = Mock(MemberRepository)
        this.memberActivityStreamMock = Mock(MemberActivityStream)
        this.messageChannelMock = Mock(MessageChannel)
        this.mapperMock = Mock(ObjectMapper)
        this.service = new CreateMemberImpl(memberRepositoryMock, memberActivityStreamMock, mapperMock)

        memberActivityStreamMock.outbound() >> messageChannelMock
    }

    def "Should throw IllegalArgumentException when member request payload is null"() {

        given:
        def memberRequest = null

        when:
        service.create(memberRequest)

        then:
        Exception exception = thrown()
        exception instanceof IllegalArgumentException
    }

    def "Should throw EmailExistsException when the email exists in repository"() {

        given:
        def memberRequest = random(MemberRequest)

        and:
        memberRepositoryMock.existsByEmail(memberRequest.email) >> true

        when:
        service.create(memberRequest)

        then:
        Exception exception = thrown()
        exception instanceof EmailExistsException
    }

    def "Should return a created member and save member/Created event"() {

        given:
        def memberRequest = random(MemberRequest)

        and:
        def savedMember = new Member()
        savedMember.id = UUID.randomUUID()
        savedMember.name = memberRequest.name
        savedMember.email = memberRequest.email

        def jsonString = "{json}"

        and:
        memberRepositoryMock.existsByEmail(memberRequest.email) >> false
        mapperMock.writeValueAsString(*_) >> jsonString

        when:
        def result = service.create(memberRequest)

        then:
        result == savedMember

        and:
        1 * memberRepositoryMock.save({
            it.id != null &&
                    it.email == memberRequest.email &&
                    it.name == memberRequest.name
        }) >> savedMember

        and:
        1 * messageChannelMock.send({
            it.getHeaders().get("X-EVENT") == "memberCreated" &&
                    it.getHeaders().get(MessageHeaders.CONTENT_TYPE) == MimeTypeUtils.APPLICATION_JSON &&
                    it.getPayload().id == savedMember.id &&
                    it.getPayload().name == savedMember.name &&
                    it.getPayload().email == savedMember.email &&
                    it.getPayload().password == memberRequest.password
        })
    }

    def "Should throw an exception when failed to publish the event message"() {

        given:
        def memberRequest = random(MemberRequest)

        and:
        def savedMember = new Member()
        def jsonString = "{json}"

        and:
        memberRepositoryMock.existsByEmail(memberRequest.email) >> false
        mapperMock.writeValueAsString(*_) >> jsonString
        messageChannelMock.send(*_) >> { throw new IllegalStateException() }

        when:
        service.create(memberRequest)

        then:
        Exception exception = thrown()
        exception instanceof IllegalStateException

        and:
        1 * memberRepositoryMock.save({
            it.id != null &&
                    it.email == memberRequest.email &&
                    it.name == memberRequest.name
        }) >> savedMember
    }
}
