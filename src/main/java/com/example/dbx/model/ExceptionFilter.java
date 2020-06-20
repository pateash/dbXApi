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

    public ExceptionSeverity getExceptionSeverity() {
        if (severity.equals("medium")) {
            return ExceptionSeverity.SEVERITY_MEDIUM;
        } else if (severity.equals("high")) {
            return ExceptionSeverity.SEVERITY_HIGH;
        }

        return ExceptionSeverity.SEVERITY_LOW;
    }

    public ExceptionStatus getExceptionStatus() {
        return status.equals("resolved") ? ExceptionStatus.STATUS_RESOLVED : ExceptionStatus.STATUS_UNRESOLVED;
    }
}
