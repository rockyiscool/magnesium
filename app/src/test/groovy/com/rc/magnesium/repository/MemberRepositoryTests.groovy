package com.rc.magnesium.repository

import com.rc.magnesium.entity.Member
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Unroll

import static com.rc.magnesium.entity.QMember.member
import static io.github.benas.randombeans.api.EnhancedRandom.random

class MemberRepositoryTests extends RepositorySpecification {

    @Autowired
    MemberRepository memberRepository

    def "Test save"() {

        given:
        def expected = new Member()
        expected.id = UUID.randomUUID()
        expected.email = "rocky.chen@ef.com"
        expected.name = "Rocky"

        when:
        memberRepository.save(expected)

        then:
        def actual = entityManager.find(Member.class, expected.id)
        actual.id == expected.id
        actual.name == expected.name
        actual.email == expected.email
        actual.createdDate == expected.createdDate
        actual.lastModifiedDate == expected.lastModifiedDate
    }

    def "Test findById"() {

        given:
        def expected = random(Member.class)
        entityManager.persist(expected)

        when:
        def actual = memberRepository.findById(expected.id)

        then:
        actual.get().id == expected.id
        actual.get().name == expected.name
        actual.get().email == expected.email
        actual.get().createdDate == expected.createdDate
        actual.get().lastModifiedDate == expected.lastModifiedDate
        actual.get().version == expected.version
    }

    @Unroll
    def "Test existsByEmail - #testCase"() {

        given:
        def member = new Member()
        member.id = UUID.randomUUID()
        member.email = "exists@email.org"
        entityManager.persist(member)

        when:
        def actual = memberRepository.existsByEmail(email)

        then:
        actual == expected

        where:
        testCase        | expected | email
        "Exists"        | true     | "exists@email.org"
        "Doesn't exist" | false    | "non-existing@email.org"
    }

    @Unroll
    def "Test exists - #testCase"() {

        given:
        def memberA = new Member()
        memberA.id = UUID.fromString("123e4567-e89b-12d3-a456-426655440001")
        memberA.email = "memberA@email.org"
        entityManager.persist(memberA)

        def memberB = new Member()
        memberB.id = UUID.fromString("123e4567-e89b-12d3-a456-426655440002")
        memberB.email = "memberB@email.org"
        entityManager.persist(memberB)

        when:
        def p1 = member.email.equalsIgnoreCase(p1Value)
        def p2 = member.id.ne(p2Value)
        def actual = memberRepository.exists(p2.and(p1))

        then:
        actual == expected

        where:
        testCase                            | expected | p1Value             | p2Value
        "update itself email address"       | false    | "memberA@email.org" | UUID.fromString("123e4567-e89b-12d3-a456-426655440001")
        "update someone else email address" | true     | "memberB@email.org" | UUID.fromString("123e4567-e89b-12d3-a456-426655440001")
    }
}
