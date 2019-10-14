package com.rc.magnesium

import com.rc.magnesium.service.MemberActivityStream
import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.stream.test.binder.MessageCollector
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import javax.sql.DataSource

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class
)
@AutoConfigureMockMvc
@ActiveProfiles("ct")
abstract class ComponentTestSpecification extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    DataSource dataSource

    @Autowired
    MemberActivityStream memberActivityStreamMock

    @Autowired
    MessageCollector collector

    Sql sql

    def setup() {
        this.sql = new Sql(dataSource.connection)
    }

    def generator = { String alphabet, int n ->
        new Random().with {
            (1..n).collect { alphabet[nextInt(alphabet.length())] }.join()
        }
    }

    def generateEmail() {
        generator((('A'..'Z') + ('0'..'9')).join(), 9) + "@test.org"
    }

}
