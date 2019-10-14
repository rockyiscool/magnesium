package com.rc.magnesium.payload.request.v1;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class MemberRequest implements Serializable {

    private static final long serialVersionUID = 2609746234943122447L;

    @NotBlank(message = "name can't empty!")
    private String name;

    @NotBlank(message = "email can't empty!")
    private String email;

    @NotBlank(message = "password can't empty!")
    private String password;

    public String getName() {
        return name;
    }

    public MemberRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public MemberRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public MemberRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return "MemberRequest{"
                + "name='" + name + "'"
                + ", email='" + email + "'"
                + ", password='" + password + "'"
                + "}";
    }
}
