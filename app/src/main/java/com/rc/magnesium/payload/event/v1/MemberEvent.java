package com.rc.magnesium.payload.event.v1;

import java.io.Serializable;
import java.util.UUID;

public class MemberEvent implements Serializable {

    private static final long serialVersionUID = 2177467272002654138L;

    private UUID id;
    private String name;
    private String email;
    private String password;

    public UUID getId() {
        return id;
    }

    public MemberEvent setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MemberEvent setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public MemberEvent setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public MemberEvent setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return "MemberEvent{"
                + "id=" + id + ", "
                + "name='" + name + "', "
                + "email='" + email + "', "
                + "password='" + password + "', "
                + "}";
    }
}
