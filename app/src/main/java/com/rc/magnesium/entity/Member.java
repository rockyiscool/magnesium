package com.rc.magnesium.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "members")
public class Member extends Auditable<String> {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;
    private String name;

    @Column(unique = true)
    private String email;

    public UUID getId() {
        return id;
    }

    public Member setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Member setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Member setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return id.equals(member.id) &&
                email.equals(member.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
