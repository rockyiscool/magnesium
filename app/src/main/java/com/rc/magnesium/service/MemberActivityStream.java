package com.rc.magnesium.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MemberActivityStream {

    String OUTPUT = "output-member-activity";

    @Output(OUTPUT)
    MessageChannel outbound();
}
