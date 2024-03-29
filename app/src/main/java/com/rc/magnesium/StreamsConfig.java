package com.rc.magnesium;

import com.rc.magnesium.service.MemberActivityStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding({
        MemberActivityStream.class
})
public class StreamsConfig {
}
