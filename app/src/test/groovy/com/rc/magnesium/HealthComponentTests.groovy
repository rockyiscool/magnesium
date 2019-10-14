package com.rc.magnesium


import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class HealthComponentTests extends ComponentTestSpecification {

    def "Should display that the service is healthy"() {

        when:
        def response = mockMvc.perform(get("/actuator/health")
                .contentType(APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
    }
}
