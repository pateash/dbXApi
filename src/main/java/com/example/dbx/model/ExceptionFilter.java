package com.example.dbx.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExceptionFilter {
    private String severity;
    private String severityOrder;
    private String status;
    private String category;
    private String source;
}
