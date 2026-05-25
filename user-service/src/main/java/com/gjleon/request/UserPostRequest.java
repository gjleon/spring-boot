package com.gjleon.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserPostRequest {
    private String firstName;
    private String lastName;
    private String email;
}
