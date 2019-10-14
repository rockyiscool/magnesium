package com.rc.magnesium.endpoint.v1;

import com.rc.magnesium.entity.Member;
import com.rc.magnesium.payload.request.v1.MemberRequest;
import com.rc.magnesium.service.CreateMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/members")
public class MemberEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(MemberEndpoint.class);

    private final CreateMember createMember;

    @Autowired
    public MemberEndpoint(CreateMember createMember) {
        this.createMember = createMember;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid MemberRequest memberRequest, UriComponentsBuilder uriBuilder) {
        logger.debug("create a member with the payload: {}", memberRequest);

        Member newMember = createMember.create(memberRequest);
        UriComponents uriComponents = uriBuilder.path("/v1/members/{id}")
                .buildAndExpand(newMember.getId());

        return ResponseEntity.created(uriComponents.toUri())
                .build();
    }
}
