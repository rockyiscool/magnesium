package com.rc.magnesium.repository;

import com.rc.magnesium.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MemberRepository extends CrudRepository<Member, UUID>, JpaRepository<Member, UUID>, QuerydslPredicateExecutor<Member> {

    boolean existsByEmail(final String email);
}
