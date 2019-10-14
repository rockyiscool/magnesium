package com.rc.magnesium.converter

import com.rc.magnesium.entity.Member
import spock.lang.Specification

import static io.github.benas.randombeans.api.EnhancedRandom.random

class MemberEventConverterTests extends Specification {

    def "Should convert a member entity to a member event"() {

        given:
        def entity = random(Member, "createdBy", "lastModifiedBy")
        entity.createdBy = "Rocky"
        entity.lastModifiedBy = "Chen"

        when:
        def result = MemberEventConverter.convertFromEntity(entity)

        then:
        result.id == entity.id
        result.name == entity.name
        result.email == entity.email
    }

    def "Should throw IllegalArgumentException because of null entity"() {

        given:
        def entity = null

        when:
        MemberEventConverter.convertFromEntity(entity)

        then:
        Exception exception = thrown()
        exception instanceof IllegalArgumentException
    }
}
