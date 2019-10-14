package com.rc.magnesium.endpoint

import com.rc.magnesium.exception.GlobalExceptionHandler
import org.springframework.context.support.StaticApplicationContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import spock.lang.Specification

class EndpointSpecification extends Specification {

    MockMvc mockMvc

    def init(endpoint) {
        final StaticApplicationContext applicationContext = new StaticApplicationContext()
        applicationContext.registerSingleton("exceptionHandler", GlobalExceptionHandler.class)

        final WebMvcConfigurationSupport webMvcConfigurationSupport = new WebMvcConfigurationSupport()
        webMvcConfigurationSupport.setApplicationContext(applicationContext)

        this.mockMvc = MockMvcBuilders.standaloneSetup(endpoint)
                .setHandlerExceptionResolvers(webMvcConfigurationSupport.handlerExceptionResolver())
                .build()
    }
}
