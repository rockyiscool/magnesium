package com.rc.magnesium.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.magnesium.EventType;
import com.rc.magnesium.converter.MemberEventConverter;
import com.rc.magnesium.entity.Member;
import com.rc.magnesium.exception.EmailExistsException;
import com.rc.magnesium.payload.event.v1.MemberEvent;
import com.rc.magnesium.payload.request.v1.MemberRequest;
import com.rc.magnesium.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.MimeTypeUtils;

import java.util.UUID;

@Service
public class CreateMemberImpl implements CreateMember {

    private final MemberRepository memberRepository;
    private final MemberActivityStream memberActivityStream;
    private final ObjectMapper mapper;

    @Autowired
    public CreateMemberImpl(MemberRepository memberRepository, MemberActivityStream memberActivityStream, ObjectMapper mapper) {
        this.memberRepository = memberRepository;
        this.memberActivityStream = memberActivityStream;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Member create(MemberRequest memberRequest) {
        Assert.notNull(memberRequest, "memberRequest must not be null");

        if (memberRepository.existsByEmail(memberRequest.getEmail())) {
            throw new EmailExistsException("Email is existence. member: %s" + memberRequest);
        }

        Member member = buildMember(memberRequest);
        Member savedMember = memberRepository.save(member);

        publishEvent(EventType.CREATED, memberRequest, savedMember);

        return savedMember;
    }

    private Member buildMember(MemberRequest memberRequest) {
        return new Member()
                .setId(UUID.randomUUID())
                .setEmail(memberRequest.getEmail())
                .setName(memberRequest.getName());
    }

    private void publishEvent(String eventType, MemberRequest memberRequest, Member persistedMember) {
        MemberEvent eventBody = MemberEventConverter.convertFromEntity(persistedMember)
                .setPassword(memberRequest.getPassword());

        Message<MemberEvent> message = MessageBuilder.withPayload(eventBody)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .setHeader("X-EVENT", eventType)
                .build();

        memberActivityStream.outbound().send(message);
    }

}
