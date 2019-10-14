package com.rc.magnesium.converter;

import com.rc.magnesium.entity.Member;
import com.rc.magnesium.payload.event.v1.MemberEvent;
import org.springframework.util.Assert;

public class MemberEventConverter {

    public static MemberEvent convertFromEntity(Member entity) {
        Assert.notNull(entity, "entity must not be null");

        return new MemberEvent()
                .setId(entity.getId())
                .setName(entity.getName())
                .setEmail(entity.getEmail());
    }
}
