package com.example.dbx.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SignupForm {
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    @NotBlank
    @Size(min = 1, max = 50)
    private String username;

    private Long orgUnit;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
}