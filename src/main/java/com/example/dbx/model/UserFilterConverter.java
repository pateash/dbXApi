package com.example.dbx.model;

import java.io.IOException;

import com.example.dbx.exception.InvalidException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserFilterConverter implements Converter<String, UserFilter> {
    private final ObjectMapper objectMapper;

    @Override
    public UserFilter convert(String source) {
        try {
            return objectMapper.readValue(source, UserFilter.class);
        } catch (IOException e) {
            throw new InvalidException(e.getMessage());
        }
    }
}