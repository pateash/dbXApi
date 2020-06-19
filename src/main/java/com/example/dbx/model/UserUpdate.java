package com.example.dbx.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdate {
    @NotBlank
    private String name;

    @NotNull
    private Boolean isEnabled;
}