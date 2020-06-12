package com.example.dbx.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ExceptionFilterConverter implements Converter<String, ExceptionFilter> {

    private final ObjectMapper objectMapper;

    public ExceptionFilterConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ExceptionFilter convert(String source) {
        try {
            return objectMapper.readValue(source, ExceptionFilter.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}