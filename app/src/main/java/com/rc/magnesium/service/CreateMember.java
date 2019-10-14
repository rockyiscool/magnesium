package com.rc.magnesium.service;

import com.rc.magnesium.entity.Member;
import com.rc.magnesium.payload.request.v1.MemberRequest;

public interface CreateMember {

    Member create(MemberRequest memberRequest);
}
